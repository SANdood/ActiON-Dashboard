SmartThings ActiON Dashboard
======

If you like this app, please support the developer:<br/> [![PayPal](https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif) alex.smart.things@gmail.com](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=A5K5L44TEF77U)

======
ActiON Dashboard is a web client for SmartThings. It is designed to run in any modern browser, regardless of operating system or screen size.

ActiON Dashboard SmartApp easily installs in 2 minues. Create as many instances as you need and pick relevant devices for each dashboard.

ActiON Dashboard has no dependency on the SmartThings Mobile app or third party services.

[Download SmartApp](https://github.com/action-dashboard/action-dashboard.github.io/blob/master/ActiON4/app.groovy)

Uses
======
* Install as a native app to your smartphone to for a quick, user friendly access to your devices.
* Install on a wall-mounted tablet as a whole-house control panel.
* Create dashboards as needed for your family and friends and decide what they are allowed to view and control.

Create SmartApp
======
1. Login to https://graph.api.smartthings.com/
* Click on My SmartApps https://graph.api.smartthings.com/ide/apps
* Click on "+ New SmartApp" on the right side
* Click on "From Code" tab
* Paste the <a href="https://github.com/action-dashboard/action-dashboard.github.io/blob/master/ActiON4/app.groovy" target="_blank">SmartApp code</a>
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
* Follow through configuration of the SmartApp
* Don't forget to click "Done"

To obtain the URL of your ActiON Dashboard

1. Open logs at https://graph.api.smartthings.com/ide/logs 
* Go to My Apps section in SmartThings Mobile app
* Tap the icon of ActiON SmartApp
* The ActiON Dashboard URL will be printed in the logs

Install app via SmartThings IDE
======

1. Login to https://graph.api.smartthings.com/
* Click on My SmartApps https://graph.api.smartthings.com/ide/apps
* Click on the name of the app you want to install
* Pick location in the drop-down on top right
* Click "Set Location" button
* Follow through configuration of the SmartApp, don't forget to get through all pages if there are more than one.
* Don't forget to click "Done"

To obtain the URL of your ActiON Dashboard

1. Open logs in another tab at https://graph.api.smartthings.com/ide/logs
* Back in the tab that had the SmartApp details, click "Trigger Now" button on right side under Preferences
* The ActiON Dashboard URL will be printed in the logs

Android and iOS native apps
=====
To install Action Dashboard as a native app, open the URL in Safari on iOS or Chrome on Android device, then add shortcut to the home screen. When the shortcut icon is tapped, the dashboard launches as a full screen native application (without toolbars).

OAuth
=====
* If you don’t want to expose the access_token, you can omit it from the URL. You will be prompted to login with your SmartThings account.
* If you need to invalidate the access_token, you can reset it via the SmartApp configuration, uninstall the SmartApp or change OAuth details in the App Settings (IDE).
