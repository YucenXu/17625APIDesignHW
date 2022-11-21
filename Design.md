## **17625 A2 Part 1**
by Yucen Xu
### **API Design Task**
### **1.1 Design subtask**

### **1.1.1 Schemas**
The schema for the API is as follows.

<img src="diagrams/ER.jpg" width="800"/>

```javascript
type Appointment {
  id: ID!
  time: Int
  patient: String
  doctor: Doctor
  patient: Patient
}

type Doctor {
  id: ID!
  name: String
  clinicName: String
  appointments: String
  appointmentList: [Appointment!]!
}

type Patient {
  name: String
  appointment: Appointment
}
```
<br></br>

### **1.1.2 Queries**
**a. Get Doctor Details**

Description: Get name, clinic of a doctor.

Input:
```javascript
type Query{
  doctorByID(uid: ID): Doctor
}
```

Output:
```javascript
{
  doctor
  {
    name
    clinic name
    appoitments {
        id
        time
        patient {
            id
            name
        }
    }
  }
}
```

**b. Get Available Timeslots**

Description: Get doctor's available timeslots for current day. Timeslot is a number from 1 to 48 (each number represents a 30 min slot, starting from 12 am)

Input:
```javascript
type Query{
  timeslotByID(uid: ID): [int]
}
```

Output:
```javascript
{
  timeslots
}
```
<br></br>

### **1.1.3 Mutations**
**a. Add Appointment**

Description: Book an appointment with doctor for today

Input:
```javascript
type Mutation{
  createAppointment(input: CreateAppointmentInput!): ID
}

input CreateAppointmentInput {
    doctorID: ID!
    patient: Patient
}
```

Output:
```javascript
{
  appointmentID
}
```

**b. Cancel Appointment**

Description: Cancel an appointment

Input:
```javascript
type Mutation{
  cancelAppointment(input: CancelAppointmentInput!): ID
}

input CancelAppointmentInput {
    appointmentID: ID!
}
```

Output:
```javascript
{
  appointmentID
}
```

**c. Update Appointment**

Description: Update name of the patient for an appointment

Input:
```javascript
type Mutation{
  updateAppointment(input: UpdateAppointmentInput!): ID
}

input UpdateAppointmentInput {
    appointmentID: ID!
    patientName: String
}
```

Output:
```javascript
{
  appointmentID
}
```
<br></br>

### **1.1.3 Endpoints**
There will be only one endpoint for the system, since in GraphQL, the query is based on types.
For local test, the endpoint will be localhost:8000.
<br></br>

### **1.2 Testcases**
| Identifier | Description | Inputs | Expected Output | Remarks |
|-|-|-|-|-|
| Get_Doctor_Success | Get details of a doctor with valid ID | Query {<br>doctorByID("D_1")<br>} | {<br>"data":{<br>"name":"doctor_xxx"<br>"clinic name":"xxx"<br>"appointments":[<br>{"id":"A_1"<br>"time":16<br>"patient":{<br>"id":"P_1"<br>"name":"patient_xxx"}<br>}<br>]<br>}<br>}||
| Get_Doctor_Fail | Get details of a doctor with invalid ID | Query {<br>doctorByID(null)<br>} | {<br>"message":"Invalid Parameter"<br>} | Server should handle error |
| Get_Time_Success | Get available timeslots of a doctor with valid ID | Query {<br>doctorByID("D_1")<br>} | {<br>"data":{<br>"timeslots":[1,2,3]<br>}<br>}||
| Get_Time_Fail | Get available timeslots of a doctor with invalid ID | Query {<br>doctorByID(null)<br>} | {<br>"message":"Invalid Parameter"<br>} | Server should handle error |
| Add_Appointment_Success | Add appointment with valid docter ID and input | Mutation {<br>CreateAppointment("D_1", "P_1")<br>} | {<br>"data":{<br>"appointment":"A_1"<br>}<br>} ||
| Add_Appointment_Fail | Add appointment with invalid input | Mutation {<br>CreateAppointment(null)<br>} | {<br>"message":"Invalid Parameter"<br>} ||
| Cancel_Appointment_Success | Cancel appointment with valid appointment ID | Mutation {<br>CancelAppointment("A_1")<br>} | {<br>"data":{<br>"appointment":"A_1"<br>}<br>} ||
| Cancel_Appointment_Fail | Cancel appointment with invalid appointment ID | Mutation {<br>CancelAppointment(null)<br>} | {<br>"message":"Invalid Parameter"<br>} ||
| Update_Appointment_Success | Update appointment with valid appointment ID and input| Mutation {<br>UpdateAppointment("A_1","Patient_Newname")<br>} | {<br>"data":{<br>"appointment":"A_1"<br>}<br>} ||
| Update_Appointment_Fail | Update appointment with invalid input| Mutation {<br>UpdateAppointment(null)<br>} | {<br>"message":"Invalid Parameter"<br>} ||