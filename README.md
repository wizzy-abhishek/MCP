This is an Agentic AI system. Here, I tried to implement a router forwards the query to LLM.
Once LLM receives the query, it configures the context of users' query, it states to the router that which tool to use.
So the MCP Host, assosiates the MCP client with the provided data from the LLM.
The MCP client is the one that communicate with the MCP server(specific tool).
They return the response according to their logic.
After this response is sent back to the LLM, again humanize the and the main user gets what they want.
