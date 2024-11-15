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
   Create the OpenAI API key in environment variables.
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

### Annotation System

The custom annotation system in FractalFathom enables precise tagging of classes, methods, and fields to specify functional roles, dependencies, and mappings to higher-level concepts. These annotations facilitate detailed feature recognition and business logic mapping in the analysis process.

#### Key Components

1. **Annotation Entities:** Define annotations applied to Java elements (classes, methods, fields) and specify properties like scope, activation phase, and dependencies.
2. **Feature and Mapping Entities:** `FeatureEntity` denotes specific functionalities, while `MappingEntity` associates code elements with architectural concepts or business logic, enabling structured analysis.
3. **Annotation Attributes and Contexts:** Provide metadata to specify properties and context, helping maintain consistency and interpretability in large codebases.

#### Example Usage

Below is an example of how to apply annotations in Java, demonstrating functionality identification and logical mapping.

```java
package org.example;

import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;

@FractalFathomFeature(name = "User Authentication", description = "Handles user login and session management", type = FeatureType.FUNCTIONAL)
public class AuthService {

    @FractalFathomFeature(name = "Encrypt Password", description = "Encrypts passwords using SHA-256", type = FeatureType.FUNCTIONAL)
    @FractalFathomMapping(toConcept = "Security")
    private String encryptPassword(String password) {
        // Password encryption logic here
    }

    @FractalFathomFeature(name = "Session Management", description = "Manages user sessions", type = FeatureType.FUNCTIONAL)
    public void manageSession(User user) {
        // Session management logic
    }
}
```

#### Explanation

- **Class-Level Annotation:** `@Feature` on `AuthService` tags the entire class with the functionality of "User Authentication," aiding the system in identifying this as a key feature within the code.
- **Method-Level Annotation:** `@Feature` on `encryptPassword` specifies the function's purpose. The `@Mapping` annotation links it to the "Security" concept, marking it as part of the security logic within the business context.
- **Multi-Method Tagging:** Additional methods like `manageSession` are tagged as separate functionalities, allowing deep analysis of each component’s role within the class.

These annotations, when processed, provide FractalFathom with a structured understanding of each component's role and interconnections, facilitating a clear and interactive visualization of complex systems in the generated PlantUML component diagrams.

---

### Start the Server

1. Run the Flask server:
   ```bash
   python3 src/main/kotlin/org/protogalaxy/fractalfathom/cli/modelInference/server.py
   ```

   Once started, the server will run on `http://localhost:5000` to handle embedding generation and PlantUML code generation requests.

### Run the Project

1. Run the FractalFathom CLI tool:
   ```bash
   ./gradlew run --args='path/to/java/project path/to/output/directory'
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