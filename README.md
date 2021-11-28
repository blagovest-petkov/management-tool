# HR management tool

## How to run - Guide

- Open terminal<br>
- Run folowing command: docker build --tag=mng-tool:latest .
- Run folowing command: docker run -p8080:8080 mng-tool:latest
- Note: Port 8080 might not be available on the machine where the container is launched. In this case, the mapping might not work and we need to choose a port that's still available.
- Open Postman (or something else)
- Execute GET request to get the employee hierarchy
  - URL: localhost:8080/employee 
  - Use Basic Auth with username: 'user' and password: '12345678'
  - Execute PoST request to get add employee to the hierarchy
  - URL: localhost:8080/employee 
  - Use Basic Auth with username: 'user' and password: '12345678'
  - Body
  ```
    {
      "Pete":"Nick",
      "Barbara":"Nick",
      "Nick":"Sophie",
      "Sophie":"Jonas"
    }
  ```
