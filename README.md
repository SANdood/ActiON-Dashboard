SmartThings ActiON Dashboard
======

If you like this app, please support the developer:<br/> [![PayPal](https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif) alex.smart.things@gmail.com](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=A5K5L44TEF77U)

======
ActiON Dashboard is a web client for SmartThings. It is designed to run in any modern browser, regardless of operating system or screen size.

ActiON Dashboard SmartApp easily installs in 2 minutes. Create as many instances as you need and pick relevant devices for each dashboard.

ActiON Dashboard has no dependency on the SmartThings Mobile app or third party services.

[Download SmartApp](https://github.com/625alex/ActiON-Dashboard/blob/master/app-v4/ActiON4SmartApp.groovy)

Uses
======
* Install as a native app to your smartphone to for a quick, user friendly access to your devices.
* Install on a wall-mounted tablet as a whole-house control panel.
* Create dashboards as needed for your family and friends and decide what they are allowed to view and control.

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

Install app via SmartThings IDE (no longer supported)
======

1. Login to [IDE](https://graph.api.smartthings.com/)
* Click on [My SmartApps]( https://graph.api.smartthings.com/ide/apps)
* Click on the name of the app you want to install
* Pick location in the drop-down on top right
* Click "Set Location" button
* ***Don't click "Install" button***
* Follow through SmartApp preferences. Click "Next" button finally click "Done".
* Dashboard URL is printed in the logs

Please not that I no longer support this feature. Installing via the IDE works, but people will have to figure it out on their own.

Android and iOS native apps
=====
To install Action Dashboard as a native app, open the URL in Safari on iOS or Chrome on Android device, then add shortcut to the home screen. When the shortcut icon is tapped, the dashboard launches as a full screen native application (without toolbars).

OAuth
=====
* If you don’t want to expose the access_token, you can omit it from the URL. You will be prompted to login with your SmartThings account.
* If you need to invalidate the access_token, you can reset it via the SmartApp preferences, uninstall the SmartApp or change OAuth details in the App Settings (IDE).

Dropcam Stream
=====
In order to display a Dropcam stream, your camera must be public. This is the only way to display a Dropcam stream. This only works on Desktop browsers since Dropcam requires Flash.

1. Login into your Dropcam account and select the camera that you want to add to the dashboard. Click on Sharing > Public. Click on "Make Public" button.
* Copy content of "Embed Video" box to a text editor.
* Inside the block of code there will be a URL that looks like 
https://www.dropcam.com/e/1234abcd?autoplay=true
* Copy this URL into your SmartApp.
