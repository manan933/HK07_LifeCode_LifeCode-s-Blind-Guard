Development of a Smart Blind Stick for Enhanced Mobility and Safety of Visually Impaired Persons
Hardware edition • use PS ID during registration

Problem
According to WHO, over 42 million people worldwide are blind, and nearly 2.2 billion have some form of visual impairment. In India, millions of visually impaired individuals face mobility challenges daily. Traditional white canes provide only limited assistance in detecting ground-level obstacles and do not alert users about overhanging objects, vehicles, or dangerous situations.
The proposed Smart Blind Stick will be an innovative mobility aid integrating ultrasonic sensors, infrared sensors, and GPS navigation to help visually impaired persons detect obstacles (both at ground and head level), potholes, and fast-approaching vehicles. It will use vibration and audio feedback to alert the user in real time. The stick will also have an emergency SOS button that can send the user’s location to a registered guardian via SMS or mobile app.
To ensure affordability and accessibility, the design will use low-cost yet durable components, rechargeable batteries, and lightweight materials. The device will be compact, easy to use, and scalable for mass production.

Objectives
Design an affordable and smart blind stick that ensures independence, safety, and improved navigation for visually impaired individuals.
Integrate ultrasonic sensors, infrared sensors, and GPS navigation.
Detect obstacles (ground/head level), potholes, and fast-approaching vehicles.
Provide vibration and audio feedback to alert the user in real time.
Emergency SOS button sends the user’s location to a registered guardian via SMS or mobile app.
Use low-cost durable components, rechargeable batteries, and lightweight materials; compact and scalable design.
Expected Outcomes
A low-cost Smart Blind Stick prototype with integrated sensors and feedback system.
Mobile app connectivity for navigation and emergency support.
Real-time obstacle detection within a range of 2–3 meters.
Lightweight, foldable, and ergonomically designed for daily use.
Affordable solution (< ₹3000 per unit) for large-scale deployment.

Software Technology used-

Kotlin — Main programming language for Android app
Android SDK — Core platform for SMS monitoring, notifications, and background execution
Room Database (SQLite ORM) — Stores last 10 emergency alerts locally (offline persistence)
Google Maps Intent Integration — Opens live emergency location directly in Maps
Andriod Studio

Communication & Network
SIM800L v2 and Neo6M

Embedded Systems
Arduino Nano — Central microcontroller coordinating all sensors and modules
MPU6050 Accelerometer/Gyroscope — Detects fall events through motion analysis
Ultrasonic Sensors (4 Units)
Front → Detects fast approaching obstacles/vehicles
Left & Right → Detect lateral dangers
Down → Detect potholes / uneven terrain

Alert System (Feedback)
Dual buzzers
Vibration motor
SOS push button
