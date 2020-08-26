# Inspections
Inspections Application featuring offline working

TODO()

<<Screens>>
============
Login Screen
============
  username
  password
  <<Functionality>>
  Login

=======================  
Inspections list Screen
=======================
  view Pager
    past Inspections
    current Inspections
    future Inspections

    <<Functionality>>
    View Inspection

========================
Inspection Detail Screen
========================
  current
    view edit save submit
  past
    view
  future
    view - slimmed down version

  <<Functionality>>
  Saving
    Save CURRENT inspection state to offline database

  submit
    if offline then submit CURRENT (completed) inspection to database with an intent to submit when online again
    if online submit CURRENT (completed) inspection to network


<<Data Types>>
Inspection Record
    Type
    Area
    List<Question>
    Status
    completed: Boolean

Question
    List<Answer> size >= 2

Answer
    Title: String
    Submit: Int 0..100
    NotApplicable: Boolean

Type <Enum>

Area <Enum>   

Status <Enum>
  PAST
  CURRENT
  FUTURE


<<Functionality>>
Retrieve Inspection Records
Fetch data from online periodically and update database
Fetch data from database to populate view data
