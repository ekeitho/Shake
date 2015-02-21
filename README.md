# Shake
CPE 409 Developing with Cloud Services
Winter 2015

Vision

	Conveniently find locations of others and optimize the process of regrouping
    Find friends at events in which separation from group is plausible e.g. music festivals, bars, shopping centers, casinos
    Find friends in general vicinity
  
Scope

	All Android and iOS users
	Events in which group separation is plausible and regrouping is an issue
  
  
General friend list

    Friends can be found via Facebook friends or phone number
    Friends can be trusted
      Can request location of trusted friends until untrusted or friend becomes hidden
    Trust circles (groups)
      All members of a trust circle are mutually trusted
      GroupMe and Facebook integration
      
 Finding friends
 
    Individual:
      Not trusted: Trackee must give permission to be tracked
      Trusted: Does not require trackee permission
    Trust circle:
      Does not require trackee permission for any trackee in circle
    Display movement flow between historical poll locations

Communication

    Photo sharing: ability to provide a visual location reference
    Group chat
    
Other features

    Force vibrations - Increases chance that receivers will check phones regardless of phone volume settings
    Shake - Shaking will automatically find locations of default trust circle

```
-- Team Details --
Product Owner: Keith Abdulla
Scrum Master: Thomas Nguyen
Rest of Team: Tam Nguyen, Tim Ramos
```

REST API's: 

    Facebook 
    Google Maps
    GroupMe

Tools:

    Parse
    taiga.io
    github
    
Technical Overview:

Coding Languages and why
    
    Java for Android Platform because it is the native language for the platform
    
Cloud Platform and why
    
    Parse for backend data storage
API structure and why
    
    Facebook Graph API to pull friends, create groups, and send requests because it is already well established and takes care of social connections
    Google Maps for maps and location services because it is already well established
    
Cloud Database used to store application data, and why.
    
    Parse for backend data storage because it is already used by Facebook API
    
Code repository used, and why.
    
    GitHub because it’s what we used in class and is a very efficient subversion control system

