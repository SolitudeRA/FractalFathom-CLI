# FractalFathom CLI

FractalFathom CLI is a Kotlin-based command-line tool designed to generate functional component diagrams for software using static code analysis and deep learning models. By integrating GraphCodeBERT and GPT-4 API, FractalFathom CLI automates the visualization of Java projects, showing complex relationships between classes, methods, fields, and components.

## Project Overview

1. **Static Code Analysis**: Parses Java source code using the Spoon framework to build intermediate representations (IR).
2. **Feature Enhancement**: Leverages GraphCodeBERT to generate embedding vectors, enhancing code representation.
3. **Automated PlantUML Diagram Generation**: Uses GPT-4 API to generate PlantUML component diagrams from enhanced IR data for visualizing project structures.
4. **Server Interaction**: Communicates with a Python server to interact with GPT-4 and GraphCodeBERT models, handling embedding generation and diagram creation.

## Dependencies

- **Programming Language**: Kotlin (JVM 21+)
- **Build Tool**: Gradle
- **Main Libraries**:
  - [Spoon](https://spoon.gforge.inria.fr/) - for Java static code analysis
  - [OkHttp](https://square.github.io/okhttp/) - HTTP client for API requests
  - [Jackson](https://github.com/FasterXML/jackson) - JSON parsing
  - [GraphCodeBERT](https://huggingface.co/microsoft/graphcodebert-base) - deep learning model for code embeddings
  - [PlantUML](https://plantuml.com/) - for generating UML diagrams
  - [GPT-4 API](https://openai.com/) - to generate PlantUML component diagram code

## Installation and Configuration

1. **Clone the Project**:
   ```bash
   git clone https://github.com/your-repository/fractalfathom-cli.git
   cd fractalfathom-cli
   ```

2. **Set Environment Variables**:
   Create a `.env` file and set the OpenAI API key.
   ```plaintext
   OPENAI_API_KEY=your_openai_api_key_here
   ```

3. **Install Dependencies**:
   Use Gradle to download and configure all dependencies.
   ```bash
   ./gradlew build
   ```

4. **Python Environment Setup**:
   The project requires a Python server for GraphCodeBERT and GPT-4 interactions. Install Flask and other dependencies:
   ```bash
   pip install -r requirements.txt
   ```

## Usage

### Start the Server

1. Run the Flask server:
   ```bash
   python3 src/main/kotlin/org/protogalaxy/fractalfathom/cli/modelInference/server.py
   ```

   Once started, the server will run on `http://localhost:5000` to handle embedding generation and PlantUML code generation requests.

### Run the Project

1. Run the FractalFathom CLI tool:
   ```bash
   ./gradlew run --args='path/to/java/project'
   ```

   The tool will automatically call server endpoints to generate enhanced IR data and PlantUML component diagram code through GraphCodeBERT and GPT-4 APIs.

## Project Structure

- **src/main/kotlin**: Kotlin source code, including CLI, analysis modules, and utility classes.
    - `FractalFathomCLI`: Entry point for CLI.
    - `modelInference`: Includes utilities for deep learning interactions, such as `GraphCodeBERTUtils` and `LLMUtils`.
    - `analysis`: Static code analysis module based on Spoon.
- **src/main/kotlin/org/protogalaxy/fractalfathom/cli/modelInference/server.py**: Python server code.
    - `server.py`: Contains APIs for embedding generation and PlantUML code generation.
- **resources**: Dependency files and configuration.

## Testing

Run tests with:
```bash
./gradlew test
```

The `GraphCodeBERTUtilsTest` and the `LLMUtilsTest` class starts the Python server and tests methods in `LLMUtils` and `GraphCodeBERTUtils`, verifying that the generated embeddings and PlantUML code meets expectations.

## Troubleshooting

- **GPT-4 API Token Limit**: To avoid exceeding token limits, IR data is chunked and processed in batches.
- **Socket Timeout**: Ensure the Python server is running and listening if experiencing connection timeouts.
- **Flask Debug Mode Warning**: Disable debug mode for production environments.

## Contributing

We welcome contributions! Feel free to submit Pull Requests or open Issues with suggestions. Help us improve FractalFathom CLI for better code comprehension and visualization.

## License

The GNU General Public License v3.0. See [LICENSE](LICENSE) for details.

---

We hope FractalFathom CLI enhances your code comprehension and visualization experience!