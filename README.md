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

The custom annotation system in FractalFathom enables precise tagging of Java classes, methods, and fields to define functional roles and their relationships to higher-level concepts. This annotation system drives the identification of functional components and their mappings in the analysis process.

#### Key Components

1. **Feature Annotations (`@FractalFathomFeature`)**: Used to define high-level functionalities of classes or interfaces, specifying their purpose and categorizing them into feature types (e.g., `FUNCTIONAL` or `NON_FUNCTIONAL`).
2. **Mapping Annotations (`@FractalFathomMapping`)**: Establish links between code elements and architectural concepts, aiding in the contextual understanding of software systems.
3. **Annotation Rules**:
    - Higher-level elements (e.g., classes, interfaces) can use both `@FractalFathomFeature` and `@FractalFathomMapping`.
    - Lower-level elements (e.g., methods, fields) are limited to `@FractalFathomMapping`.

#### Example Usage

Below is an example demonstrating the usage of these annotations in a real-world Java application.

```java
package org.protogalaxy.fractalfathom.cli.resources;

import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;
import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;

@FractalFathomFeature(name = "UserService", description = "Handles user-related operations", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(toConcept = "User Management", type = MappingType.MODULE)
public class UserService {

    @FractalFathomMapping(toConcept = "User Repository", type = MappingType.COMPONENT)
    private UserRepository userRepository;

    @FractalFathomMapping(toConcept = "Role Service Interaction", type = MappingType.COMPONENT)
    private RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @FractalFathomMapping(toConcept = "User Creation", type = MappingType.COMPONENT)
    public void createUser(String username, String email, String role) {
        userRepository.save(new User(username, email));
        
        roleService.assignRole(username, role);
    }

    @FractalFathomMapping(toConcept = "User Deletion", type = MappingType.COMPONENT)
    public void deleteUser(String username) {
        userRepository.delete(username);
        
        roleService.revokeAllRoles(username);
    }
}
```

#### Explanation

- **Class-Level Feature Annotation**: `@FractalFathomFeature` on `UserService` defines it as a high-level functionality handling "User Management."
- **Class-Level Mapping Annotation**: `@FractalFathomMapping` maps the `UserService` class to the architectural concept "User Management."
- **Field-Level Mapping Annotations**: Fields `userRepository` and `roleService` are mapped to respective components in the system architecture.
- **Method-Level Mapping Annotations**: Methods like `createUser` and `deleteUser` are mapped to specific functional components, providing detailed insight into their roles within the larger context.

These annotations help FractalFathom perform structured analysis, producing accurate and visually comprehensible diagrams for understanding complex software architectures.

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