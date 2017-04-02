# Android Teacher Attendance Application

This application is aimed at solving the problem 'Unavailability of Teachers in Government Schools' which is a part of Smart India Hackathon, 2017.

## Permissions:

> Bluetooth
> GPS
> Fingerprint(optional above Android N)
> Internet Usage
> Wifi State

## Dependencies (Libraries):

> retrofit
> converter-gson
> graphview
> glide
> reprint (fingerprint authentication)

## Steps for attendance

> Login
> Tap 'Mark Attendance'
> Fingerprint authentication (if Android Version > N and fingerprint sensors are present)
> Bluetooth devices fetching starts automatically in parallel
> GPS location detection also starts in parallel
> Both fetched GPS location and Bluetooth device ids are sent to the server to mark the attendance
