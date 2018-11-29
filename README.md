# PatientList - Bonus Version

Technology Used : Java, FHIR, Android Studio

Libraries Used : ButterKnife, Room, Android Architecture components, Glide

# Overview

  - The app is based on the MVVM architecture
  - The web requests are made using the FHIR OKHTTP Client (fhir-client-okhttp) and data modelling is done through (hapi-fhir-structures-dstu3) framework
  - The images are served using the Glide Library
  - Unit Tests & Instrumented Tests are done using JUnit

# Workflow

   - Launch the app.
   - Wait for the data to get loaded from the network.
   - Once the data is loaded and saved to database, the list is populated with the data (10 items)
   - The user can search through the list by clicking the search button and entering the text. The data is searched by the name.
   - The user can also filter the data by name, gender and birthdate.
   - Upon clicking on a Patient, the user can edit the name, gender and birthdate (This is not synced back to the server).
   - When the user clicks back, the patient data is saved to the database and updated on the list.

# Unit Tests

   - Unit Tests can be found in the test (com.parithi.patientlist) folder

### Demo

![Demo](play.gif)

### Class Structure

![Class Structure](ClassStructure.png)

### Screens

#### List Screen

![List](1.png)

#### Search Screen
![Search](2.png)

#### Filter Screen
![Filter](3.png)

#### Edit Screen
![Edit](4.png)

#### Change Date Screen
![Change Date](5.png)

