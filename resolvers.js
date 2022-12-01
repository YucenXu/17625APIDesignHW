const db = require('./db')

const Doctor = {
    appointments: (doctor) => db.appointments.filter(a => a.doctorID === doctor.id)
}

const Patient = {
    appointments: (patient) => db.appointments.filter(a => a.patientID === patient.id)
}

const Appointment = {
    patient: (appointment) => db.patients.find(p => p.id === appointment.patientID),
    doctor: (appointment) => db.doctors.find(d => d.id === appointment.doctorID)
}

const Query = {
    doctors: () => db.doctors,
    patients: () => db.patients,
    doctorByID(parent, args, context, info) {
        const { id } = args;
        return db.doctors.find(d => d.id === id)
    },
    availableTimeslotByID(parent, args, context, info) {
        const { id } = args;
        const doctor = db.doctors.find(d => d.id === id)
        if (typeof doctor === "undefined") {
            return null
        }
        const appointments = db.appointments.filter(a => a.doctorID === id)
        let result = new Set()
        for (let i = 1; i <= 48; i++) {
            result.add(i)
        }
        for (let i = 0; i < appointments.length; i++) {
            let appointment = appointments[i]
            result.delete(appointment.time)
        }
        return Array.from(result)
    }
}

const Mutation = {
    createAppointment(parent, args, context, info) {
        const payload = args['input']
        const doctorID = payload['doctorID']
        const patientID = payload['patientID']
        const time = payload['time']
        const appointments = db.appointments.filter(a => a.doctorID === doctorID)
        // console.log(appointments)
        for (let i = 0; i < appointments.length; i++) {
            if (appointments[i].time === time) return {
                id: -1,
                time: -1,
                doctorID: -1,
                patientID: -1
            }
        }
        const lastID = appointments[appointments.length - 1]['id']
        const lastIDDigit = lastID.charAt(lastID.length - 1)
        const newID = "A_" + (parseInt(lastIDDigit) + 1)
        const newAppointment = {
            id: newID,
            time: time,
            doctorID: doctorID,
            patientID: patientID
        }
        db.appointments.push(newAppointment)
        // console.log(db.appointments)
        return newAppointment
    },
    cancelAppointment(parent, args, context, info) {
        const payload = args['input']
        const appointmentID = payload['appointmentID']
        const appointment = db.appointments.find(a => a.id === appointmentID)
        if (typeof appointment === 'undefined') {
            return {
                id: -1,
                time: -1,
                doctorID: -1,
                patientID: -1
            }
        }
        const idx = db.appointments.indexOf(appointment)
        db.appointments.splice(idx, 1)
        return appointment
    },
    updateAppointment(parent, args, context, info) {
        const payload = args['input']
        const appointmentID = payload['appointmentID']
        const newName = payload['patientName']
        const appointment = db.appointments.find(a => a.id === appointmentID)
        if (typeof appointment === 'undefined' || typeof newName === 'undefined') {
            return {
                id: -1,
                time: -1,
                doctorID: -1,
                patientID: -1
            }
        }
        
        const patient = db.patients.find(p => p.id === appointment.patientID)
        patient.name = newName
        // console.log(db.appointments)
        // console.log(db.patients)
        return appointment
    }
}

module.exports = {
    Doctor,
    Patient,
    Appointment,
    Query,
    Mutation
}
