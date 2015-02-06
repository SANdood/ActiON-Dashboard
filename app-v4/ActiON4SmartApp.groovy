/**
 *  ActiON Dashboard 4.6.2
 *
 *  Visit Home Page for more information:
 *  http://action-dashboard.github.io/
 *
 *  If you like this app, please support the developer via PayPal: alex.smart.things@gmail.com
 *
 *  This software if free for Private Use. You may use and modify the software without distributing it.
 *  
 *  This software and derivatives may not be used for commercial purposes.
 *  You may not modify, distribute or sublicense this software.
 *  You may not grant a sublicense to modify and distribute this software to third parties not included in the license.
 *
 *  Software is provided without warranty and the software author/license owner cannot be held liable for damages.
 *
 *  Copyright © 2014 Alex Malikov
 *
 *  Support for Foscam and Generic MJPEG video streams by k3v0
 *
 */
definition(
    name: "ActiON4.6.2",
    namespace: "625alex",
    author: "Alex Malikov",
    description: "ActiON Dashboard, a SmartThings web client.",
    category: "Convenience",
    iconUrl: "http://action-dashboard.github.io/icon.png",
    iconX2Url: "http://action-dashboard.github.io/icon.png",
    oauth: true)


preferences {
	page(name: "selectDevices", install: false, uninstall: true, nextPage: "viewURL") {
    
        section("About") {
            paragraph "ActiON Dashboard, a SmartThings web client.\n\nYour home has a Home Page!™"
            paragraph "Version 4.6.2\n\n" +
            "If you like this app, please support the developer via PayPal:\n\nalex.smart.things@gmail.com\n\n" +
            "Copyright © 2014 Alex Malikov"
			href url:"http://action-dashboard.github.io", style:"embedded", required:false, title:"More information...", description:"http://action-dashboard.github.io"
        }
		
		section("Things...") {
			href "controlThings", title:"View and control these things"
		}
		
        section("Video Streams...") {
			href "videoStreams", title:"Configure Dropcam video streams"
			href "videoStreamsMJPEG", title:"Configure generic MJPEG video streams", description: "Foscam, Blue Iris, etc"
		}
		
		section("Shortcuts...") {
			href "dashboards", title:"Link other dashboards"
			href "links", title:"Configure shortcuts"
		}
		
		section("More Tiles and Preferences...") {
			href "moreTiles", title:"Clock, Mode, Hello Home!"
			href "preferences", title: "Preferences"
		}
    }
	
	page(name: "controlThings", title: "controlThings")
	page(name: "videoStreams", title: "videoStreams")
	page(name: "videoStreamsMJPEG", title: "videoStreamsMJPEG")
	page(name: "dashboards", title: "dashboards")
	page(name: "links", title: "links")
	page(name: "moreTiles", title: "moreTiles")
	page(name: "preferences", title: "preferences")
	page(name: "authenticationPreferences", title: "authenticationPreferences")
	page(name: "viewURL", title: "viewURL")
}

def controlThings() {
	dynamicPage(name: "controlThings", title: "Things", install: false) {
		section("Control these things...") {
			input "holiday", "capability.switch", title: "Which Holiday Lights?", multiple: true, required: false
			input "lights", "capability.switch", title: "Which Lights?", multiple: true, required: false
			input "switches", "capability.switch", title: "Which Switches?", multiple: true, required: false
			input "dimmers", "capability.switchLevel", title: "Which Dimmers?", multiple: true, required: false
			input "momentaries", "capability.momentary", title: "Which Momentary Switches?", multiple: true, required: false
			input "locks", "capability.lock", title: "Which Locks?", multiple: true, required: false
			input "camera", "capability.imageCapture", title: "Which Cameras?", multiple: true, required: false
			input "music", "capability.musicPlayer", title: "Which Music Players?", multiple: true, required: false
		}
		
		section("View state of these things...") {
            input "contacts", "capability.contactSensor", title: "Which Contact?", multiple: true, required: false
            input "presence", "capability.presenceSensor", title: "Which Presence?", multiple: true, required: false
            input "temperature", "capability.temperatureMeasurement", title: "Which Temperature?", multiple: true, required: false
            input "humidity", "capability.relativeHumidityMeasurement", title: "Which Hygrometer?", multiple: true, required: false
            input "motion", "capability.motionSensor", title: "Which Motion?", multiple: true, required: false
            input "water", "capability.waterSensor", title: "Which Water Sensors?", multiple: true, required: false
            input "battery", "capability.battery", title: "Which Battery Status?", multiple: true, required: false
            input "energy", "capability.energyMeter", title: "Which Energy Meters?", multiple: true, required: false
            input "power", "capability.powerMeter", title: "Which Power Meters?", multiple: true, required: false
            input "weather", "device.smartweatherStationTile", title: "Which Weather?", multiple: true, required: false
        }
	}
}

def videoStreams() {
	dynamicPage(name: "videoStreams", title: "Video Streams", install: false) {
		section("About") {
			paragraph "Enter absolute URL of the stream starting with http..."
			href url:"http://action-dashboard.github.io", style:"embedded", required:false, title:"More information..."
		}
		
		(1..10).each{
			def title = "dropcamStreamT$it"
			def link = "dropcamStreamUrl$it"
			section("Dropcam Video Stream $it") {
				input title, "text", title:"Title", required: false
				input link, "text", title:"URL", required: false
			}
		}
	}
}

def videoStreamsMJPEG() {
	dynamicPage(name: "videoStreamsMJPEG", title: "Generic MJPEG Video Streams", install: false) {
		section("About") {
			paragraph "Enter absolute URL starting with http..."
			paragraph "For Foscam cameras use http://DOMAIN:PORT/videostream.cgi?&user=USERNAME&pwd=PASSWORD"
			paragraph "For BlueIris cameras use http://blueirisserver/mjpg/CAMERASHORTNAME/video.mjpeg"
			paragraph "Feel free to try other links for MJPEG Video Streams, your experience may vary.\n\nThere may be issues displaying these video streams using Chrome in iOS."
			href url:"http://action-dashboard.github.io", style:"embedded", required:false, title:"More information..."
		}
		
		(1..10).each{
			def title = "mjpegStreamTitile$it"
			def link = "mjpegStreamUrl$it"
			section("Generic MJPEG Video Stream $it") {
				input title, "text", title:"Title", required: false
				input link, "text", title:"URL", required: false
			}
		}
	}
}

def links() {
	dynamicPage(name: "links", title: "Shortcuts", install: false) {
		section() {
			paragraph "Enter absolute URL starting with http..."
		}
		
		(1..10).each{
			def title = "linkTitle$it"
			def link = "linkUrl$it"
			log.debug "t: $t, l: $l"
			section("Link $it") {
				input title, "text", title:"Title", required: false
				input link, "text", title:"URL", required: false
			}
		}
	}
}

def dashboards() {
	dynamicPage(name: "dashboards", title: "Dashboards", install: false) {
		section() {
			paragraph "Enter absolute URL starting with https..."
		}
		(1..10).each{
			def title = "dashboardTitle$it"
			def link = "dashboardUrl$it"
			log.debug "t: $t, l: $l"
			section("Dashboard $it") {
				input title, "text", title:"Title", required: false
				input link, "text", title:"URL", required: false
			}
		}
	}
}

def moreTiles() {
	dynamicPage(name: "moreTiles", title: "More Tiles...", install: false) {
		section() {
			input "showMode", title: "Mode", "bool", required: true, defaultValue: true
			input "showHelloHome", title: "Hello, Home! Actions", "bool", required: true, defaultValue: true
			input "showClock", title: "Clock", "enum", multiple: false, required: true, defaultValue: "Small Analog", options: ["Small Analog", "Small Digital", "Large Analog", "Large Digital", "None"]
		}
	}
}

def preferences() {
	dynamicPage(name: "preferences", title: "Preferences...", install: false) {
		section("Preferences...") {
			label title: "Title", required: false, defaultValue: "ActiON4"
			input "roundNumbers", title: "Round Off Decimals", "bool", required: true, defaultValue:true
			input "dropShadow", title: "Drop Shadow", "bool", required: true, defaultValue: true
			input "tileSize", title: "Tile Size", "enum", multiple: false, required: true, defaultValue: "Normal", options: ["Small", "Normal", "Large"]
			input "fontSize", title: "Font Size", "enum", multiple: false, required: true, defaultValue: "Normal", options: ["Normal", "Larger", "Largest"]
			input "holidayType", title: "Holiday Lights Theme", "enum", multiple: false, required: true, defaultValue: "Christmas", options: ["Christmas", "Valentine's"]
		}
		
		if (state) {
			section() {
				href url:"${generateURL("list").join()}", style:"embedded", required:false, title:"Device Order", description:"Tap to change, then click \"Done\""
			}
		}
		
		section() {
			href "authenticationPreferences", title:"Access and Authentication"
		}
	}
}

def authenticationPreferences() {
	dynamicPage(name: "authenticationPreferences", title: "Access and Authentication", install: false) {
		section() {
			input "disableDashboard", "bool", title: "Disable temporarily (hide all tiles)?", defaultValue: false, required:false
			input "readOnlyMode", "bool", title: "View only mode?", defaultValue: false, required:false
		}
		section("Reset Access Token...") {
        	paragraph "Activating this option will invalidate access token. Access to all authenticated instances of this dashboard will be permanently revoked."
        	input "resetOauth", "bool", title: "Reset Access Token?", defaultValue: false
        }
	}
}

def viewURL() {
	dynamicPage(name: "viewURL", title: "ActiON Dashboard", install:!resetOauth, nextPage: resetOauth ? "viewURL" : null) {
		if (resetOauth) {
			generateURL(null)
			
			section("Reset Access Token...") {
				paragraph "You chose to reset Access Token in ActiON Dashboard preferences."
				href "authenticationPreferences", title:"Reset Access Token", description: "Tap to set this option to \"OFF\""
			}
		} else {
			section() {
				paragraph "Copy the URL below to any modern browser to view ${title ?: location.name} ActiON Dashboard. Add a shortcut to home screen of your mobile device to run as a native app."
				href url:"${generateURL("link").join()}", style:"embedded", required:false, title:"URL", description:"Tap to view, then click \"Done\""
			}
			
			section("Send URL via SMS...") {
				paragraph "Optionally, send SMS containing the URL of this dashboard to a phone number. The URL will be sent in two parts because it's too long."
				input "phone", "phone", title: "Which phone?", required: false
			}
		}
    }
}

mappings {
	if (params.access_token && params.access_token != state.accessToken) {
        path("/ui") {action: [GET: "oauthError"]}
        path("/command") {action: [GET: "oauthError"]}
        path("/data") {action: [GET: "oauthError"]}
        path("/ping") {action: [GET: "oauthError"]}
        path("/link") {action: [GET: "oauthError"]}
        path("/list") {action: [GET: "oauthError"]}
        path("/position") {action: [GET: "oauthError"]}
	} else if (!params.access_token) {
		path("/ui") {action: [GET: "html"]}
        path("/command") {action: [GET: "command"]}
        path("/data") {action: [GET: "allDeviceData"]}
        path("/ping") {action: [GET: "ping"]}
        path("/link") {action: [GET: "viewLinkError"]}
        path("/list") {action: [GET: "list"]}
		path("/position") {action: [GET: "position"]}
	} else {
        path("/ui") {action: [GET: "html"]}
        path("/command") {action: [GET: "command"]}
        path("/data") {action: [GET: "allDeviceData"]}
        path("/ping") {action: [GET: "ping"]}
        path("/link") {action: [GET: "link"]}
		path("/list") {action: [GET: "list"]}
		path("/position") {action: [GET: "position"]}
    }
}

def oauthError() {[error: "OAuth token is invalid or access has been revoked"]}

def viewLinkError() {[error: "You are not authorized to view OAuth access token"]}

def command() {
	log.debug "command received with params $params"
    
	if (disableDashboard || readOnlyMode) return [status: "disabled"]
	
    def id = params.device
    def type = params.type
    def command = params.command
	def value = params.value

	def device
    
	if (type == "switch") {
		device = switches?.find{it.id == id}
		if (device) {
			if(device.currentValue('switch') == "on") {
				device.off()
			} else {
				device.on()
			}
		}
	} else if (type == "light") {
		device = lights?.find{it.id == id}
		if (device) {
			if(device.currentValue('switch') == "on") {
				device.off()
			} else {
				device.on()
			}
		}
	} else if (type == "holiday") {
		device = holiday?.find{it.id == id}
		if (device) {
			if(device.currentValue('switch') == "on") {
				device.off()
			} else {
				device.on()
			}
		}
	} else if (type == "lock") {
		device = locks?.find{it.id == id}
		if (device) {
			if(device.currentValue('lock') == "locked") {
                device.unlock()
            } else {
                device.lock()
            }
		}
	} else if (type == "dimmer") {
		device = dimmers?.find{it.id == id}
		if (device) {
			if (command == "toggle") {
				if(device.currentValue('switch') == "on") {
					device.off()
				} else {
					device.setLevel(Math.min((value as Integer) * 10, 99))
				}
			} else if (command == "level") {
				device.setLevel(Math.min((value as Integer) * 10, 99))
			}
		}
    } else if (type == "mode") {
		setLocationMode(command)
	} else if (type == "helloHome") {
        log.debug "executing Hello Home '$value'"
    	location.helloHome.execute(command)
    } else if (type == "momentary") {
    	momentaries?.find{it.id == id}?.push()
    } else if (type == "camera") {
    	camera?.find{it.id == id}.take()
    } else if (type == "music") {
		device = music?.find{it.id == id}
		if (device) {
			if (command == "level") {
				device.setLevel(Math.min((value as Integer) * 10, 99))
			} else {
				device."$command"()
			}
		}
	}
    
	[status:"ok"]
}

def position() {
	log.debug "command received with params $params"
	def map = [:]
	params?.list?.split("\\|~\\|").eachWithIndex{o, i -> map[o] = i}
	state.sortOrder = map
	log.debug "state.sortOrder: $state.sortOrder"
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
    scheduledWeatherRefresh()
    sendURL_SMS("ui")
	
	updateStateTS()
	
	subscribe(location, handler)
	subscribe(holiday, "switch.on", handler, [filterEvents: false])
	subscribe(holiday, "switch.off", handler, [filterEvents: false])
	subscribe(holiday, "switch", handler, [filterEvents: false])
	subscribe(holiday, "level", handler, [filterEvents: false])
	subscribe(lights, "switch.on", handler, [filterEvents: false])
	subscribe(lights, "switch.off", handler, [filterEvents: false])
	subscribe(lights, "switch", handler, [filterEvents: false])
	subscribe(lights, "level", handler, [filterEvents: false])
    subscribe(switches, "switch", handler, [filterEvents: false])
    subscribe(dimmers, "level", handler, [filterEvents: false])
	subscribe(dimmers, "switch", handler, [filterEvents: false])
    subscribe(locks, "lock", handler, [filterEvents: false])
    subscribe(contacts, "contact", handler, [filterEvents: false])
    subscribe(presence, "presence", handler, [filterEvents: false])
    subscribe(temperature, "temperature", handler, [filterEvents: false])
    subscribe(humidity, "humidity", handler, [filterEvents: false])
    subscribe(motion, "motion", handler, [filterEvents: false])
    subscribe(water, "water", handler, [filterEvents: false])
    subscribe(battery, "battery", handler, [filterEvents: false])
    subscribe(energy, "energy", handler, [filterEvents: false])
    subscribe(power, "power", handler, [filterEvents: false])

	subscribe(music, "status", handler, [filterEvents: false])
	subscribe(music, "level", handler, [filterEvents: false])
	subscribe(music, "trackDescription", handler, [filterEvents: false])
	subscribe(music, "mute", handler, [filterEvents: false])
}

def sendURL_SMS(path) {
	generateURL(path)
	if (state.accessToken) {
		log.info "${title ?: location.name} ActiON Dashboard URL: ${generateURL("ui").join()}"
		if (phone) {
			sendSmsMessage(phone, generateURL(path)[0])
			sendSmsMessage(phone, generateURL(path)[1])
		}
	}
}

def generateURL(path) {
	log.debug "resetOauth: $resetOauth"
	if (resetOauth) {
		log.debug "Reseting Access Token"
		state.accessToken = null
	}
	
	if (!resetOauth && !state.accessToken || resetOauth && !state.accessToken) {
		try {
			createAccessToken()
			log.debug "Creating new Access Token: $state.accessToken"
		} catch (ex) {
			log.error "Did you forget to enable OAuth in SmartApp IDE settings for ActiON Dashboard?"
			log.error ex
		}
	}
	
	["https://graph.api.smartthings.com/api/smartapps/installations/${app.id}/$path", "?access_token=${state.accessToken}"]
}

def scheduledWeatherRefresh() {
    runIn(3600, scheduledWeatherRefresh, [overwrite: false])
	weather?.refresh()
    state.lastWeatherRefresh = getTS()
	updateStateTS()
}

def head() {
"""
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<link rel="icon" sizes="192x192" href="https://action-dashboard.github.io/icon.png">
<link rel="apple-touch-icon" href="https://action-dashboard.github.io/icon.png">
<meta name="mobile-web-app-capable" content="yes">
<title>${app.label ?: location.name}</title>

<link rel="stylesheet" href="https://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.css" />
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/weather-icons/1.3.2/css/weather-icons.min.css" />
<link href="https://625alex.github.io/ActiON-Dashboard/prod/style.4.6.0.min.css?u=0" rel="stylesheet">
<link href='https://fonts.googleapis.com/css?family=Mallanna' rel='stylesheet' type='text/css'>

<script>
var stateTS = ${getStateTS()};
var tileSize = ${getTSize()};
var readOnlyMode = ${readOnlyMode ?: false};
var icons = ${getTileIcons().encodeAsJSON()};
var smartAppVersion = "4.6.2";
</script>

<script src="https://code.jquery.com/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="https://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.js" type="text/javascript"></script>
<script src="https://625alex.github.io/ActiON-Dashboard/prod/script.4.6.0.min.js?u=0" type="text/javascript"></script>

<style>
.tile {width: ${getTSize()}px; height: ${getTSize()}px;}
.w2 {width: ${getTSize() * 2}px;}
.h2 {height: ${getTSize() * 2}px;}
${!dropShadow ? ".icon, .icon * {text-shadow: none;} .ui-slider-handle.ui-btn.ui-shadow {box-shadow: none; -webkit-box-shadow: none; -moz-box-shadow: none;}" : ""}
body {font-size: ${getFSize()}%;}
${readOnlyMode ? """.tile, .music i {cursor: default} .clock, .refresh{cursor: pointer}""" : ""}
${getHolidayIcon().css}
</style>
"""
}                                                              

def footer() {
"""<script>
\$(function() {
  var wall = new freewall(".tiles");
  wall.fitWidth();
  
  wall.reset({
			draggable: false,
			selector: '.tile',
		animate: true,
		gutterX:cellGutter,
		gutterY:cellGutter,
		cellW:cellSize,
		cellH:cellSize,
		fixSize:null,
		onResize: function() {
			wall.fitWidth();
			wall.refresh();
		}
	});
	wall.fitWidth();
	// for scroll bar appear;
	\$(window).trigger("resize");
});
</script>"""
}

def headList() {
"""
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0"/>
<title>${app.label ?: location.name} Device Order</title>

<link rel="stylesheet" href="https://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.css" />
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/weather-icons/1.3.2/css/weather-icons.min.css" />
<link href="https://625alex.github.io/ActiON-Dashboard/prod/style.4.6.0.min.css?u=0" rel="stylesheet">
<link href='https://fonts.googleapis.com/css?family=Mallanna' rel='stylesheet' type='text/css'>

<script src="https://code.jquery.com/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="https://code.jquery.com/ui/1.11.2/jquery-ui.min.js" type="text/javascript"></script>
<script src="https://625alex.github.io/ActiON-Dashboard/jquery.ui.touch-punch.min.js" type="text/javascript"></script>

<script>
	\$(function() {
		\$( ".list" ).sortable({
			stop: function( event, ui ) {changeOrder();}
		});
		\$( ".list" ).disableSelection();
	});

	function changeOrder() {
		var l = "";
		\$( ".list li" ).each(function(index) {
			l = l + \$(this).data("type") + "-" + \$(this).data("device") + "|~|";
		});
		var access_token = getUrlParameter("access_token");
		var request = {list: l};
		if (access_token) request["access_token"] = access_token;
		
		\$.get("position", request).done(function(data) {
			if (data.status == "ok") {}
		}).fail(function() {alert("error, please refresh")});
	}
	
	function getUrlParameter(sParam)
	{
		var sPageURL = window.location.search.substring(1);
		var sURLVariables = sPageURL.split('&');
		for (var i = 0; i < sURLVariables.length; i++) 
		{
			var sParameterName = sURLVariables[i].split('=');
			if (sParameterName[0] == sParam) 
			{
				return sParameterName[1];
			}
		}
	}
</script>
<style>
ul{list-style-type: none;padding-left:0;}
* {color: white;font-size:20px;}
.batt {background-size: 20px 20px;}
.item {cursor:grab; padding:5px; margin:8px;border-radius:2px}
.list {width: 75%; margin: 0 auto 60px auto;}
.list i {margin-right:5px;}
${getHolidayIcon().css}
</style>
"""
}  

def getTSize() {
	if (tileSize == "Small") return 105
	if (tileSize == "Large") return 150
	120
}

def getFSize() {
	if (fontSize == "Larger") return 120
	if (fontSize == "Largest") return 150
	100
}

def getTS() {
	def tf = new java.text.SimpleDateFormat("h:mm a")
    if (location?.timeZone) tf.setTimeZone(location.timeZone)
    "${tf.format(new Date())}"
}

def getDate() {
	def tf = new java.text.SimpleDateFormat("MMMMM d")
    if (location?.timeZone) tf.setTimeZone(location.timeZone)
    "${tf.format(new Date())}"
}

def getDOW() {
	def tf = new java.text.SimpleDateFormat("EEEE")
    if (location?.timeZone) tf.setTimeZone(location.timeZone)
    "${tf.format(new Date())}"
}

def renderModeTile(data) {
"""<div class="mode tile w2 menu ${data.isStandardMode ? data.mode : ""}" data-mode="$data.mode" data-popup="mode-popup">
	<div class="title">Mode</div>
	<div data-role="popup" id="mode-popup" data-overlay-theme="b">
		<ul data-role="listview" data-inset="true" style="min-width:210px;">
			${data.modes.collect{"""<li data-icon="false">$it</li>"""}.join("\n")}
		</ul>
    </div>
	<div class="icon Home"><i class="fa fa-home"></i></div>
	<div class="icon Night"><i class="fa fa-moon-o"></i></div>
	<div class="icon Away"><i class="fa fa-sign-out"></i></div>
	<div class="icon small text mode-name" id="mode-name">$data.mode</div>
</div>"""
}

def renderHelloHomeTile(data) {
"""
<div class="hello-home tile menu" data-rel="popup" data-popup="hello-home-popup">
	<div class="title">Hello, Home!</div>
	<div data-role="popup" id="hello-home-popup" data-overlay-theme="b">
		<ul data-role="listview" data-inset="true" style="min-width:210px;">
			${data.phrases.collect{"""<li data-icon="false">$it</li>"""}.join("\n")}
		</ul>
	</div>
</div>
"""
}

def roundNumber(num) {
	if (!roundNumbers || !"$num".isNumber()) return num
	if (num == null || num == "") return "n/a"
	else {
    	try {
            return "$num".toDouble().round()
        } catch (e) {return num}
    }
}

def getWeatherData(device) {
	def data = [tile:"device", active:"inactive", type: "weather", device: device.id, name: device.displayName]
    ["city", "weather", "feelsLike", "temperature", "localSunrise", "localSunset", "percentPrecip", "humidity", "weatherIcon"].each{data["$it"] = device?.currentValue("$it")}
    data.icon = ["chanceflurries":"wi-snow","chancerain":"wi-rain","chancesleet":"wi-rain-mix","chancesnow":"wi-snow","chancetstorms":"wi-storm-showers","clear":"wi-day-sunny","cloudy":"wi-cloudy","flurries":"wi-snow","fog":"wi-fog","hazy":"wi-dust","mostlycloudy":"wi-cloudy","mostlysunny":"wi-day-sunny","partlycloudy":"wi-day-cloudy","partlysunny":"wi-day-cloudy","rain":"wi-rai","sleet":"wi-rain-mix","snow":"wi-snow","sunny":"wi-day-sunny","tstorms":"wi-storm-showers","nt_chanceflurries":"wi-snow","nt_chancerain":"wi-rain","nt_chancesleet":"wi-rain-mix","nt_chancesnow":"wi-snow","nt_chancetstorms":"wi-storm-showers","nt_clear":"wi-stars","nt_cloudy":"wi-cloudy","nt_flurries":"wi-snow","nt_fog":"wi-fog","nt_hazy":"wi-dust","nt_mostlycloudy":"wi-night-cloudy","nt_mostlysunny":"wi-night-cloudy","nt_partlycloudy":"wi-night-cloudy","nt_partlysunny":"wi-night-cloudy","nt_sleet":"wi-rain-mix","nt_rain":"wi-rain","nt_snow":"wi-snow","nt_sunny":"wi-night-clear","nt_tstorms":"wi-storm-showers","wi-horizon":"wi-horizon"][data.weatherIcon]
	data
}

def renderTile(data) {
	if (data.type == "weather"){
		def city = data.city
		data.remove("city")
		return """<div class="weather tile w2" data-type="weather" data-device="$data.device" data-city="$city" data-weather='${data.encodeAsJSON()}'></div>"""
	} else if (data.type == "music") {
		return """
		<div class="music tile w2 $data.active ${data.mute ? "muted" : ""}" data-type="music" data-device="$data.device" data-level="$data.level" data-track-description="$data.trackDescription" data-mute="$data.mute">
			<div class="title"><span class="name">$data.name</span><br/><span class='title2 track'>$data.trackDescription</span></div>
			<div class="icon text"><i class="fa fa-fw fa-backward back"></i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-fw fa-pause pause"></i><i class="fa fa-fw fa-play play"></i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-fw fa-forward forward"></i></div>
			<div class="footer"><i class='fa fa-fw fa-volume-down unmuted'></i><i class='fa fa-fw fa-volume-off muted'></i></div>
		</div>
		"""
	} else if (data.tile == "device") {
		return """<div class="$data.type tile $data.active" data-active="$data.active" data-type="$data.type" data-device="$data.device" data-value="$data.value" data-level="$data.level" data-is-value="$data.isValue"><div class="title">$data.name</div></div>"""
	} else if (data.tile == "link") {
		return """<div class="link tile" data-link-i="$data.i"><div class="title">$data.name</div><div class="icon"><a href="$data.link" data-ajax="false" style="color:white"><i class="fa fa-th"></i></a></div></div>"""
	} else if (data.tile == "dashboard") {
		return """<div class="dashboard tile" data-link-i="$data.i"><div class="title">$data.name</div><div class="icon"><a href="$data.link" data-ajax="false" style="color:white"><i class="fa fa-link"></i></a></div></div>"""
	} else if (data.tile == "video") {
		return """<div class="video tile h2 w2" data-link-i="$data.i"><div class="title">$data.name</div><div class="icon" style="margin-top:-82px;"><object width="240" height="164"><param name="movie" value="$data.link"></param><param name="allowFullScreen" value="true"></param><param name="allowscriptaccess" value="always"></param><param name="wmode" value="opaque"></param><embed src="$data.link" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="240" height="164" wmode="opaque"></embed></object></div></div>"""
	} else if (data.tile == "genericMJPEGvideo") {
		return """<div class="video tile h2 w2" data-link-i="$data.i"><div class="title">$data.name</div><div class="icon" style="margin-top:-82px;"><object width="240" height="164"><img src="$data.link" width="240" height="164"></object></div></div>"""
	} else if (data.tile == "refresh") {
		return """<div class="refresh tile clickable"><div class="title">Refresh</div><div class="footer">Updated $data.ts</div></div>"""
	} else if (data.tile == "mode") {
		return renderModeTile(data)
	} else if (data.tile == "clock") {
		if (data.style == "a") {
			return """<div id="analog-clock" class="clock tile clickable h$data.size w$data.size"><div class="title">$data.date</div><div class="icon" style="margin-top:-${data.size * 45}px;"><canvas id="clockid" class="CoolClock:st:${45 * data.size}"></canvas></div><div class="footer">$data.dow</div></div>"""
		} else {
			return """<div id="digital-clock" class="clock tile clickable w$data.size"><div class="title">$data.date</div><div class="icon ${data.size == 2 ? "" : "text"}" id="clock">*</div><div class="footer">$data.dow</div></div>"""
		}
	} else if (data.tile == "helloHome") {
		return renderHelloHomeTile(data)
	}
	
	return ""
}

def getTileIcons() {
	[
		dimmer : [off : "<i class='inactive fa fa-fw fa-toggle-off'></i>", on : "<i class='active fa fa-fw fa-toggle-on'></i>"],
		switch : [off : "<i class='inactive fa fa-fw fa-toggle-off'></i>", on : "<i class='active fa fa-fw fa-fw fa-toggle-on'></i>"],
		light : [off : "<i class='inactive opaque fa fa-fw fa-lightbulb-o'></i>", on : "<i class='active fa fa-fw fa-lightbulb-o'></i>"],
		lock : [locked : "<i class='inactive fa fa-fw fa-lock'></i>", unlocked : "<i class='active fa fa-fw fa-unlock-alt'></i>"],
		motion : [active : "<i class='active fa fa-fw fa-exchange'></i>", inactive: "<i class='inactive opaque fa fa-fw fa-exchange'></i>"],
		presence : [present : "<i class='active fa fa-fw fa-map-marker'></i>", notPresent: "<i class='inactive opaque fa fa-fw fa-map-marker'></i>"],
		contact : [open : "<i class='active r45 fa fa-fw fa-expand'></i>", closed: "<i class='inactive r45 fa fa-fw fa-compress'></i>"],
		water : [dry : "<i class='inactive fa fa-fw fa-tint'></i>", wet: "<i class='active fa fa-fw fa-tint'></i>"],
		momentary : "<i class='fa fa-fw fa-circle-o'></i>",
		camera : "<i class='fa fa-fw fa-camera'></i>",
		refresh : "<i class='fa fa-fw fa-refresh'></i>",
		humidity : "<i class='fa fa-fw wi wi-sprinkles'></i>",
		temperature : "<i class='fa fa-fw wi wi-thermometer'></i>",
		energy : "<i class='fa fa-fw wi wi-lightning'></i>",
		power : "<i class='fa fa-fw fa-bolt'></i>",
		battery : "<i class='fa fa-fw fa-fw batt'></i>",
        helloHome : "<i class='fa fa-fw fa-comment-o'></i>",
        link : "<i class='fa fa-fw fa-link'></i>",
        dashboard : "<i class='fa fa-fw fa-th'></i>",
		holiday: getHolidayIcon()
	]
}

def getListIcon(type) {
	def icons = [
		clock: """<i class="fa fa-fw fa-clock-o"></i>""",
		mode: """<i class="fa fa-fw fa-gear"></i>""",
		weather: """<i class="fa fa-fw fa-sun-o"></i>""",
		music: """<i class="fa fa-fw fa-music"></i>""",
		video: """<i class="fa fa-fw fa-video-camera"></i>""",
		"hello-home": getTileIcons().helloHome,
		lock: getTileIcons().lock.locked,
		switch: getTileIcons().switch.on,
		light: getTileIcons().light.on,
		holiday: getTileIcons().holiday.on,
		dimmer: getTileIcons().dimmer.on,
		momentary: getTileIcons().momentary,
		contact: getTileIcons().contact.open,
		presence: getTileIcons().presence.present,
		motion: getTileIcons().motion.active,
		camera: getTileIcons().camera,
		temperature: getTileIcons().temperature,
		humidity: getTileIcons().humidity,
		water: getTileIcons().humidity,
		energy: getTileIcons().energy,
		power: getTileIcons().power,
		battery: getTileIcons().battery,
		link: getTileIcons().link,
		dashboard: getTileIcons().dashboard,
		refresh: getTileIcons().refresh,
	]
	
	icons[type]
}

def getHolidayIcon() {
	def icons = [
	"Valentine's" : [on : "<i class='active fa fa-fw fa-heart'></i>", off : "<i class='inactive fa fa-fw fa-heart-o'></i>", css: ".holiday {background-color: #FF82B2;} /*pink*/ .holiday.active {background-color: #A90000} .holiday.active .icon i {color:#EA001F}"],
	"Christmas" : [on: "<i class='active fa fa-fw fa-tree'></i>", off: "<i class='inactive fa fa-fw fa-tree'></i>", css: ".holiday {background-color: #11772D;} /*green*/ .holiday.active {background-color: #AB0F0B} .holiday.active .icon i {color:#11772D}"],
	"default" : [off : "<i class='inactive opaque fa fa-fw fa-lightbulb-o'></i>", on : "<i class='active fa fa-fw fa-lightbulb-o'></i>", css : ""]
    ]
	icons[holidayType ?: "default"]
}

def renderListItem(data) {return """<li class="item $data.type" data-type="$data.type" data-device="$data.device" id="$data.type|$data.device">${getListIcon(data.type)}$data.name</li>"""}

def getMusicPlayerData(device) {[tile: "device", type: "music", device: device.id, name: device.displayName, status: device.currentValue("status"), level: getDeviceLevel(device, "music"), trackDescription: device.currentValue("trackDescription"), mute: device.currentValue("mute") == "muted", active: device.currentValue("status") == "playing" ? "active" : ""]}

def getDeviceData(device, type) {[tile: "device",  active: isActive(device, type), type: type, device: device.id, name: device.displayName, value: getDeviceValue(device, type), level: getDeviceLevel(device, type), isValue: isValue(device, type)]}

def getDeviceFieldMap() {[lock: "lock", holiday: "switch", light: "switch", "switch": "switch", dimmer: "switch", contact: "contact", presence: "presence", temperature: "temperature", humidity: "humidity", motion: "motion", water: "water", power: "power", energy: "energy", battery: "battery"]}

def getActiveDeviceMap() {[lock: "unlocked", holiday: "on", light: "on", "switch": "on", dimmer: "on", contact: "open", presence: "present", motion: "active", water: "wet"]}

def isValue(device, type) {!(["momentary", "camera"] << getActiveDeviceMap().keySet()).flatten().contains(type)}

def isActive(device, type) {
	def field = getDeviceFieldMap()[type]
	def value = "n/a"
	try {
		value = device.respondsTo("currentValue") ? device.currentValue(field) : device.value
	} catch (e) {
		log.error "Device $device ($type) does not report $field properly. This is probably due to numerical value returned as text"
	}
	value == getActiveDeviceMap()[type] ? "active" : "inactive"
}

def getDeviceValue(device, type) {
	def unitMap = [temperature: "°", humidity: "%", battery: "%", power: "W", energy: "kWh"]
	def field = getDeviceFieldMap()[type]
	def value = "n/a"
	try {
		value = device.respondsTo("currentValue") ? device.currentValue(field) : device.value
	} catch (e) {
		log.error "Device $device ($type) does not report $field properly. This is probably due to numerical value returned as text"
	}
	if (!isValue(device, type)) return value
	else return "${roundNumber(value)}${unitMap[type] ?: ""}"
}

def getDeviceLevel(device, type) {
if (type == "dimmer" ||  type == "music") return "${(device.currentValue("level") ?: 0) / 10.0}".toDouble().round() ?: 1}

def handler(e) {
	log.debug "event happened $e.description"
	updateStateTS()
}

def updateStateTS() {state.ts = now()}

def getStateTS() {state.ts}

def ping() {
	if ("$params.ts" == "${getStateTS()}") [status: "noop", updated: getTS(), ts: getStateTS()]
	else [status: "update", updated: getTS(), ts: getStateTS(), data: allDeviceData()]
}

def allDeviceData() {
	def refresh = [tile: "refresh", ts: getTS(), name: "Refresh", type: "refresh"]
	if (disableDashboard) return [refresh]
	
	def data = []
	
	if (showClock == "Small Analog") data << [tile: "clock", size: 1, style: "a", date: getDate(), dow: getDOW(), name: "Clock", type: "clock"]
	else if (showClock == "Large Analog") data << [tile: "clock", size: 2, style: "a", date: getDate(), dow: getDOW(), name: "Clock", type: "clock"]
    else if (showClock == "Small Digital") data << [tile: "clock", size: 1, style: "d", date: getDate(), dow: getDOW(), name: "Clock", type: "clock"]
	else if (showClock == "Large Digital") data << [tile: "clock", size: 2, style: "d", date: getDate(), dow: getDOW(), name: "Clock", type: "clock"]
	
	if (showMode && location.modes) data << [tile: "mode", mode: "$location.mode", isStandardMode: ("$location.mode" == "Home" || "$location.mode" == "Away" || "$location.mode" == "Night"), modes: location?.modes?.name?.sort(), name: "Mode", type: "mode"]
	
	def phrases = location?.helloHome?.getPhrases()*.label?.sort()
	if (showHelloHome && phrases) data << [tile: "helloHome", phrases: phrases, name: "Hello, Home!", type: "hello-home"]
	
	weather?.each{data << getWeatherData(it)}
	
	holiday?.each{data << getDeviceData(it, "holiday")}
	lights?.each{data << getDeviceData(it, "light")}
	locks?.each{data << getDeviceData(it, "lock")}
	music?.each{data << getMusicPlayerData(it)}
	switches?.each{data << getDeviceData(it, "switch")}
	dimmers?.each{data << getDeviceData(it, "dimmer")}
	momentaries?.each{data << getDeviceData(it, "momentary")}
	contacts?.each{data << getDeviceData(it, "contact")}
	presence?.each{data << getDeviceData(it, "presence")}
	motion?.each{data << getDeviceData(it, "motion")}
	camera?.each{data << getDeviceData(it, "camera")}
	(1..10).each{if (settings["dropcamStreamUrl$it"]) {data << [tile: "video", link: settings["dropcamStreamUrl$it"], name: settings["dropcamStreamT$it"] ?: "Stream $it", i: it, type: "video"]}}
	(1..10).each{if (settings["mjpegStreamUrl$it"]) {data << [tile: "genericMJPEGvideo", link: settings["mjpegStreamUrl$it"], name: settings["mjpegStreamTitile$it"] ?: "Stream $it", i: it, type: "video"]}}
	temperature?.each{data << getDeviceData(it, "temperature")}
	humidity?.each{data << getDeviceData(it, "humidity")}
	water?.each{data << getDeviceData(it, "water")}
	energy?.each{data << getDeviceData(it, "energy")}
	power?.each{data << getDeviceData(it, "power")}
	battery?.each{data << getDeviceData(it, "battery")}
	
	(1..10).each{if (settings["linkUrl$it"]) {data << [tile: "link", link: settings["linkUrl$it"], name: settings["linkTitle$it"] ?: "Link $it", i: it, type: "link"]}}
	(1..10).each{if (settings["dashboardUrl$it"]) {data << [tile: "dashboard", link: settings["dashboardUrl$it"], name: settings["dashboardTitle$it"] ?: "Dashboard $it", i: it, type: "dashboard"]}}
	
	data << refresh
	
	data.sort{state?.sortOrder?."$it.type-$it.device"}
}

def html() {render contentType: "text/html", data: "<!DOCTYPE html><html><head>${head()}${customCSS()}</head><body style='background-color:black'>\n${renderTiles()}\n${renderWTFCloud()}${footer()}</body></html>"}
def renderTiles() {"""<div class="tiles">\n${allDeviceData()?.collect{renderTile(it)}.join("\n")}<div class="blank tile"></div></div>"""}

def renderWTFCloud() {"""<div data-role="popup" id="wtfcloud-popup" data-overlay-theme="b" class="wtfcloud"><div class="icon cloud" onclick="clearWTFCloud()"><i class="fa fa-cloud"></i></div><div class="icon message" onclick="clearWTFCloud()"><i class="fa fa-question"></i><i class="fa fa-exclamation"></i><i class='fa fa-refresh'></i></div></div>"""}

def link() {render contentType: "text/html", data: """<!DOCTYPE html><html><head><meta charset="UTF-8" />
<meta name="viewport" content="user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, height=device-height, target-densitydpi=device-dpi" /></head><body>${title ?: location.name} ActiON Dashboard URL:<br/><textarea rows="9" cols="30" style="font-size:10px;">${generateURL("ui").join()}</textarea><br/><br/>Copy the URL above and click Done.<br/></body></html>"""}

def list() {render contentType: "text/html", data: """<!DOCTYPE html><html><head>${headList()}</head><body style='background-color:black; color: white'><ul class="list">\n${allDeviceData()?.collect{renderListItem(it)}.join("\n")}</ul></body></html>"""}

def customCSS() {
"""
<style>
/*** Custonm CSS Start ***/

/*** Custonm CSS End *****/
</style>
"""
}
