# Final Idea

### Features
1. Automatic tracking browser between time declared -> analyze quality time spent

2. Form log -> Self Analysis:
    - Give all Activities (Currently browser only)
    - Personal track of Goal
    - Personal grade of Quality & Distraction
    - Solution used

3. AI Analysis based on Log:
- Send to AI service?
- AI Grade of Quality & Distraction
- 3 sentences solutions

4. Weekly Report



## ------ Revise Architecture -------
1. Frontend: User submits a form. The application enters a "loading" state.
2. Service 1 (API Gateway/Initial Handler): Receives the form data, validates it, and sends a 202 Accepted response back to the frontend.
3. Message Queue: Service 1 publishes a message with the form data to a message queue (e.g., RabbitMQ, AWS SQS).
4. Worker Service: A dedicated service (or pool of services) subscribes to the queue. It picks up the message, makes the API call to ChatGPT, and handles any potential errors or retries.
5. Database: The worker service stores the result from the ChatGPT API in a database.
6. Real-Time Notification: The backend triggers a real-time event (e.g., via WebSockets or Server-Sent Events) to the frontend.
7. Frontend: The frontend receives the event and the associated data, and updates the UI to display the result to the user.


## ------ AWS Configuration --------
Lots of stupid stuff here.

### VPC
Still do not understand this much as I have zero knowledge about networking. 
1. A virtual network setup as LAN. IP range within this VPC network only, and exposed to the internet another IP address
2. Subnets: Public and Private. 
   - **Public** means that we could access this resources via internet -> public router IP address -> public subnet. 
   - **Priv** subnet could only be accessed through other resources within VPC.
3. Security Groups: Firewall rules for resources within VPC.


### ECS
Lots of stuff about Docker + Container | Cluster -> Task -> Service | CI/CD

1. CI/CD flow:
   - Github Actions: triggered with **"master"** branch push 
   - Build Docker image locally -> Push to ECR (required AWS credential)
   - Task-Definition (image, memory/cpu, ...)
   - Deploy ECS

2. Credential AWS:
   - IAM user
   - IAM role: permissions to access AWS resources for services

3. ECS Cluster: (groups of EC2/Fargate instances that run task?)
   - Task: config for the machine + image to run on the machine 
   - Service: run the task on the cluster
   - Container: the actual application running inside the task

--> The idea is that: service will run the task: include creating instances + run the image -> container.

What are other stupid configuration:
1. CloudWatch - barely have an idea
2. New config of a service - barely get an idea of what it is...
3. VPC config - terrible !!!

-> On config of VPC:
1. Route table (so sad, did not understand at all :)))
2. Public/Private subnet:
3. Internet Gateway.

Clearly this is my understand:
Priavte: target == other resources within VPC (10.0.0.0/16), destination == local
Public: target == (0.0.0.0/0), destination == internet gateway (IGW)

Config Route table?
Route table vs Security Group:
- Route table: control the traffic flow in and out of the VPC
- Security Group: control the traffic flow in and out of the resources within VPC

