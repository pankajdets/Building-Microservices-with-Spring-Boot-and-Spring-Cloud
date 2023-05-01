import axios from 'axios'

const EMPLOYEE_SERVICE_BASE_URL = "https://localhost:9191/api/employees";

const EMPLOYEE_ID = 2;

class EmployeeService{
    getEmployee(){
        //Return response of REST API
       return axios.get(EMPLOYEE_SERVICE_BASE_URL + '/' + EMPLOYEE_ID);
    }
}
export default new EmployeeService