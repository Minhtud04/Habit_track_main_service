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


