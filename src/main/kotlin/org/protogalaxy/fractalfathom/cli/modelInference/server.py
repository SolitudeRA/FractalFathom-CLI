from flask import Flask, request, jsonify
from openai import OpenAI
from transformers import RobertaTokenizer, RobertaModel
import torch
import tiktoken

app = Flask(__name__)

# Initialize GraphCodeBERT
tokenizer = RobertaTokenizer.from_pretrained("microsoft/graphcodebert-base")
model = RobertaModel.from_pretrained("microsoft/graphcodebert-base")

"""
==================================================================================================================

Generate embeddings for code snippets using GraphCodeBERT.

==================================================================================================================
"""
@app.route('/generate_embeddings', methods=['POST'])
def generate_embeddings():
    """
    Generate an embedding vector for a single code snippet.

    Parameters:
        - code_snippet (str): The code snippet to embed.
        - embedding_size (int): Desired size of the embedding vector.

    Returns:
        - List[float]: The reduced embedding vector.
    """
    data = request.get_json()
    ir_entities = data['ir_entities']  # Expected format: {'ir_entities': [{'id': 'entity_id', 'code_snippet': '...'}, ...]}
    embeddings = {}

    for entity in ir_entities:
        entity_id = entity['id']
        code_snippet = entity['code_snippet']  # Already merged with context on the Kotlin side
        embedding = get_embedding(code_snippet)
        embeddings[entity_id] = reduce_embedding(embedding)

    return jsonify({'embeddings': embeddings})


def get_embedding(code_snippet, embedding_size=32):
    """
    Generate an embedding vector for a single code snippet.

    Parameters:
        - code_snippet (str): The code snippet to embed.
        - embedding_size (int): Desired size of the embedding vector.

    Returns:
        - List[float]: The reduced embedding vector.
    """
    inputs = tokenizer(code_snippet, return_tensors='pt', truncation=True, max_length=512)
    outputs = model(**inputs)
    last_hidden_states = outputs.last_hidden_state
    # Mean pooling
    embedding = torch.mean(last_hidden_states, dim=1).squeeze().tolist()
    # Reduce the embedding dimension to the desired size
    reduced_embedding = embedding[:embedding_size]
    return reduced_embedding


def reduce_embedding(embedding, target_dim=32):
    """
    Reduce the embedding dimension to balance size and performance.
    """
    return embedding[:target_dim]

"""
==================================================================================================================

Generate PlantUML component diagram code based on code structure data and requirements using GPT-4.

==================================================================================================================
"""
@app.route('/generate_plantuml', methods=['POST'])
def generate_plantuml():
    """
    Endpoint to generate PlantUML code for component diagrams using GPT-4.

    Expects JSON data in the format:
    {
        'prompt': '...',
        'api_key': '...'
    }

    Returns:
    {
        'plantuml_code': '...'
    }
    """
    data = request.get_json()

    # Prepare the prompt for GPT-4
    prompt = data['prompt']

    token_count = count_tokens(prompt, target_model="chatgpt-4o-latest")

    # Log the token count
    app.logger.info(f"Token count for prompt: {token_count}")

    client = OpenAI(
        api_key=data['api_key'],
    )

    # Call the GPT-4 API
    response = client.chat.completions.create(
        model="chatgpt-4o-latest",
        messages=[
            {"role": "system", "content": "You are an assistant that generates PlantUML component diagrams based on code structure data."},
            {"role": "user", "content": prompt}
        ],
        max_tokens=16384 - token_count,
        temperature=0,
        n=1
    )

    plantuml_code = response.choices[0].message.content

    return jsonify({'plantuml_code': plantuml_code})

def count_tokens(text, target_model="chatgpt-4o-latest"):
    """
    Count the number of tokens in a given text using tiktoken.

    Parameters:
        - text (str): The input text to count tokens for.
        - target_model (str): The model to use for tokenization.

    Returns:
        - int: The token count.
    """
    encoding = tiktoken.encoding_for_model(target_model)
    return len(encoding.encode(text))

if __name__ == '__main__':
    app.run(debug=True)
