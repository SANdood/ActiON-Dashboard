ActiON Dashboard, a SmartThings web client
======
**Your home has a Home Page!™**


If you like this app, please support the developer:<br/> [![PayPal](https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif) alex.smart.things@gmail.com](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=A5K5L44TEF77U)

======
ActiON Dashboard is a web client for SmartThings. It is designed to run in any modern browser, regardless of operating system or screen size.

ActiON Dashboard SmartApp easily installs in 2 minutes. Create as many different instances as you need and pick relevant devices for each dashboard.

ActiON Dashboard has no dependency on third party services.

[Download SmartApp](https://github.com/625alex/ActiON-Dashboard/blob/master/app-v4/ActiON4SmartApp.groovy)

Uses
======
* Install as a native app on your smartphone for a quick, user friendly access to your devices.
* Install on a wall-mounted tablet as a whole-house control panel.
* Create dashboards, as needed, for your family and friends and decide what they are allowed to view and control.

(https://raw.githubusercontent.com/625alex/ActiON-Dashboard/gh-pages/sample.png)

Install SmartApp in the IDE
======
1. Login to [IDE](https://graph.api.smartthings.com/)
* Click on [My SmartApps]( https://graph.api.smartthings.com/ide/apps)
* Click on "+ New SmartApp" on the right side
* Click on "From Code" tab
* Paste the [SmartApp code](https://github.com/625alex/ActiON-Dashboard/blob/master/app-v4/ActiON4SmartApp.groovy)
* Click "Create" button on the bottom of the page
* Click "App Settings" button on top right of the page
* Click on "OAuth" link toward the bottom of the page
* Click on "Enable OAuth in Smart App"
* Click "Update" button
* Click on "Code" button on top right of the page
* Click "Publish" button then "For Me" option

Install app via SmartThings Mobile app (recommended method)
======

1. Go to Dashboard of SmartThings Mobile app
* Tap (+) on the bottom of the app
* Swipe all the way to the right to My Apps
* Pick the app that you created
* Follow through SmartApp preferences and copy the dashboard URL
* Don't forget to click "Done"

Android and iOS native apps
=====
To install ActiON Dashboard as a native app, open the URL in Safari on iOS or Chrome on Android device, then add shortcut to the home screen. When the shortcut icon is tapped, the dashboard launches as a full screen native application (without tool bars).

OAuth
=====
* If you don’t want to expose the access_token, you can omit it from the URL. You will be prompted to login with your SmartThings account.
* If you need to invalidate the access_token, you can reset it via the SmartApp preferences. Go to SmartThings Mobile App > My Apps > tap the instance you want to change > Preferences

Dropcam Stream
=====
In order to display a Dropcam stream, your camera must be public. This is the only way to display a Dropcam stream. This only works on Desktop browsers since Dropcam requires Flash.

1. Login into your Dropcam account and select the camera that you want to add to the dashboard. Click on Sharing > Public. Click on "Make Public" button.
* Copy content of "Embed Video" box to a text editor.
* Inside the block of code there will be a URL that looks like 
https://www.dropcam.com/e/1234abcd?autoplay=true
* Copy this URL into your SmartApp.

Troubleshooting
=====
-	Make sure you didn’t forget to enable OAuth when you installed the app in the IDE (see above).
-	If you are upgrading from a previous version, make sure to step through app preferences and tap "Done". You don't actually need to make any changes to the preferences.

If neither of these is applicable, please contact the developer.

Tips and tricks
=====
*	If you think your tiles are too small/big you can try to change app preferences. If you don't achieve a desired result, add "&t=120" at the end of your dashboard's URL and play with the value to see what works best for you. If you are using a desktop browser you can also use zoom in/out.
*	There were feature requests for one switch to control a group of switches. This can be achieved with Big Switch app by ST. You can find it on your mobile app. Tap (+) > More > Convenience > Scroll to the bottom and find "The Big Switch" app. Then set up the master and slave switches.
* If you want to create a virtual (master) switches, go to IDE > My Devices > New Device. Enter required fields and choose "On/Off Button Tile" in the type election box. Then you can use it as any other switch and put it on the dashboard.
*	If you want multiple instances of the dashboard, install SmartApp only once in the IDE. Then, create as many instances as necessary in the ST Mobile App. Each instance has its own set of preferences.
*	Since the app is designed to scale across a range of screen sizes, you get to define only linear order, the tiles will wrap to the next line depending on available space. If you want certain tile to appear on certain row/column for a particular screen size, just count the position considering sizes of other tiles.
*	If you want to change the style, I added a block of code for your custom CSS overrides. Add your style within customCSS() method. I will always keep it at the end of the SmartApp.
*	To add weather tile, do to IDE > My Devices, add SmartWeather Tile. It will be listed as one of your devices and you could pick it when you configure preferences of ActiON Dashboard.

License
=====
This software if free for Private Use. You may use and modify the software without distributing it.

This software and derivatives may not be used for commercial purposes.
You may not modify, distribute or sublicense this software.
You may not grant a sublicense to modify and distribute this software to third parties.

Software is provided without warranty and the software author/license owner cannot be held liable for damages.

Copyright © 2014 Alex Malikov
