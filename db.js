const doctors = [{
    id: "D_1",
    name: "Doctor1",
    clinicName: "Clinic1"
}, {
    id: "D_2",
    name: "Doctor2",
    clinicName: "Clinic1"
}, {
    id: "D_3",
    name: "Doctor3",
    clinicName: "Clinic2"
}]

const appointments = [{
    id: "A_1",
    time: 16,
    doctorID: "D_1",
    patientID: "P_1"
}, {
    id: "A_2",
    time: 18,
    doctorID: "D_1",
    patientID: "P_2"
}]

const patients = [{
    id: "P_1",
    name: "Patient1"
}, {
    id: "P_2",
    name: "Patient2"
}]

module.exports = {
    doctors,
    patients,
    appointments
}
