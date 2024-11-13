from flask import Flask, request, jsonify
from openai import OpenAI, api_key
from transformers import RobertaTokenizer, RobertaModel
import torch
import openai
import os

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
    data = request.get_json()
    ir_entities = data['ir_entities']  # Expected format: {'ir_entities': [{'id': 'entity_id', 'code_snippet': '...'}, ...]}
    embeddings = {}

    for entity in ir_entities:
        entity_id = entity['id']
        code_snippet = entity['code_snippet']
        embedding = get_embedding(code_snippet)
        embeddings[entity_id] = reduce_embedding(embedding)

    return jsonify({'embeddings': embeddings})

def get_embedding(code_snippet, embedding_size=128):
    inputs = tokenizer(code_snippet, return_tensors='pt', truncation=True, max_length=512)
    outputs = model(**inputs)
    last_hidden_states = outputs.last_hidden_state
    # Mean pooling
    embedding = torch.mean(last_hidden_states, dim=1).squeeze().tolist()
    # Reduce the embedding dimension to the desired size
    reduced_embedding = embedding[:embedding_size]
    return reduced_embedding

def reduce_embedding(embedding, target_dim=128):
    # Reduce the embedding dimension to balance size and performance
    return embedding[:target_dim]

"""
==================================================================================================================

Generate PlantUML component diagram code based on code structure data and requirements using GPT-4.

==================================================================================================================
"""
@app.route('/generate_plantuml', methods=['POST'])
def generate_plantuml():
    data = request.get_json()
    ir_classes = data['ir_classes']

    # Prepare the prompt for GPT-4
    prompt = construct_prompt(ir_classes)

    client = OpenAI(
        api_key=os.getenv('OPENAI_API_KEY'),
    )

    # Call the GPT-4 API
    response = client.chat.completions.create(
        model="chatgpt-4o-latest",
        messages=[
            {"role": "system", "content": "You are an assistant that generates PlantUML component diagrams based on code structure data."},
            {"role": "user", "content": prompt}
        ],
        max_tokens=16384,
        temperature=0,
        n=1
    )

    plantuml_code = response.choices[0].message.content

    return jsonify({'plantuml_code': plantuml_code})

def construct_prompt(ir_classes):
    # Construct the prompt with requirements and IR data
    prompt = "Based on the following code structure data, generate a PlantUML component diagram code that meets the following requirements:\n"

    # Include detailed requirements
    prompt += """
1. The diagram should accurately represent the Java classes, methods, fields, and their relationships (inheritance, implementation, dependency).
2. Clearly show the functional hierarchy, grouping components according to their features and mappings.
3. Display annotations and labels for key functional features and business logic mappings.
4. Ensure the diagram is clear, well-structured, and suitable for rendering and understanding.
5. Use appropriate PlantUML syntax, so that the output can be directly rendered.
6. The diagram should be suitable for inclusion in IntelliJ IDEA's PlantUML plugin, and support users clicking components to navigate to code.
7. Simplify the diagram if necessary to avoid excessive complexity, while ensuring key components and relationships are shown.
"""

    # Include IR data in the prompt
    prompt += "\nCode Structure Data:\n"

    for ir_class in ir_classes:
        class_info = f"Class: {ir_class.get('name')} (Type: {ir_class.get('type')})\n"
        features = ir_class.get('features', [])
        mappings = ir_class.get('mappings', [])
        relations = ir_class.get('relations', [])
        methods = ir_class.get('methods', [])
        fields = ir_class.get('fields', [])

        if features:
            class_info += "Features:\n"
            for feature in features:
                class_info += f"- {feature.get('name')}: {feature.get('description', '')}\n"
        if mappings:
            class_info += "Mappings:\n"
            for mapping in mappings:
                class_info += f"- {mapping.get('toConcept')}\n"
        if relations:
            class_info += "Relations:\n"
            for relation in relations:
                class_info += f"- {relation.get('relationType')}: {relation.get('targetClass')}\n"
        if fields:
            class_info += "Fields:\n"
            for field in fields:
                class_info += f"- {field.get('modifiers', '')} {field.get('type')} {field.get('name')}\n"
        if methods:
            class_info += "Methods:\n"
            for method in methods:
                class_info += f"- {method.get('modifiers', '')} {method.get('returnType')} {method.get('name')}()\n"

        prompt += class_info + "\n"

    prompt += "\nPlease generate the PlantUML component diagram code accordingly."

    return prompt

if __name__ == '__main__':
    app.run(debug=False)
