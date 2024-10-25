# AST Rule Engine Backend in Spring Boot
A simple 3-tier rule engine application(Simple UI, API and Backend, Data) to determine 
user eligibility based on attributes like age, department, income, spend etc.The system can use 
Abstract Syntax Tree (AST) to represent conditional rules and allow for dynamic
creation,combination, and modification of these rules.<br>
<br>
**Data Structure:**<br>
 - type: String indicating the node type ("operator" for AND/OR, "operand" for conditions) <br>
 - left: Reference to another Node (left child)<br>
 - right: Reference to another Node (right child for operators)<br>
 - value: Optional value for operand nodes (e.g., number for comparisons)<br>

# Video Demo
https://youtu.be/Y23-LTRAWsM?si=ABVrp2SRyRXrzZvo

## Getting Started

### Prerequisites
- **Java SDK 17 or above** and **Maven** installed on your system.
- **MongoDB** installed or a connection URI from MongoDB Atlas.

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Garv-M/AST-RuleEngine-Backend.git
   cd AST-RuleEngine-Backend

2. **Connect your Mongo DB cluster**
   ```bash
   src/main/resources/application.properties
3. **Run the Backend Server**
   ```bash
   mvn spring-boot:run

## End Points
- `POST /create` - To create an AST true for the given Rule string</br>
- `POST /combine` - To Combine 2 Rules.</br>
- `POST /evaluate` - To Evaluate the Rule based on the User data.</br>
- `GET /{ruleId}/view`- To View the Rule string for the given ruleID AST tree</br>
- `GET /{ruleId}/update`- To Update the Rule string for the given ruleID AST tree</br>
- `GET /allRules`- To get all the Rules in the database</br>

## Frontend
https://github.com/Garv-M/AST-RuleEngine-App
