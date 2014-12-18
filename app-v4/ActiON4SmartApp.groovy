/**
 *  ActiON Dashboard 4.0
 *
 *  Visit Home Page for more information:
 *  http://action-dashboard.github.io/
 *
 *  If you like this app, please support the developer via PayPal: alex.smart.things@gmail.com
 *
 *  Copyright © 2014 Alex Malikov
 *
 */
definition(
    name: "ActiON4",
    namespace: "625alex",
    author: "Alex Malikov",
    description: "ActiON Dashboard, a SmartThings web client.",
    category: "Convenience",
    iconUrl: "http://action-dashboard.github.io/icon.png",
    iconX2Url: "http://action-dashboard.github.io/icon.png",
    oauth: true)


preferences {
	page(name: "selectDevices", title: "Devices", install: false, uninstall: true, nextPage: "selectPhrases") {
    
        section("About") {
            paragraph "ActiON Dashboard, a SmartThings web client."
            paragraph "Version 4.0\n\n" +
            "If you like this app, please support the developer via PayPal:\nalex.smart.things@gmail.com\n\n" +
            "Copyright © 2014 Alex Malikov"
			href url:"http://action-dashboard.github.io", style:"embedded", required:false, title:"More information...", description:"http://action-dashboard.github.io"
        }
        
    	section("Allow control of these things...") {
            input "holiday", "capability.switch", title: "Which Holiday Lights?", multiple: true, required: false
            input "switches", "capability.switch", title: "Which Switches?", multiple: true, required: false
            input "dimmers", "capability.switchLevel", title: "Which Dimmers?", multiple: true, required: false
            input "momentaries", "capability.momentary", title: "Which Momentary Switches?", multiple: true, required: false
            input "locks", "capability.lock", title: "Which Locks?", multiple: true, required: false
			input "camera", "capability.imageCapture", title: "Which Cameras?", multiple: true, required: false
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
    
	page(name: "selectPhrases", title: "Tiles", install: false, uninstall: true, nextPage: "selectPreferences") {
		section("Show...") {
        	input "showMode", title: "Show Mode", "bool", required: true, defaultValue: true
			input "showHelloHome", title: "Show Hello, Home! Actions", "bool", required: true, defaultValue: true
            input "showClock", title: "Show Clock", "enum", multiple: false, required: true, defaultValue: "Small Analog", options: ["Small Analog", "Small Digital", "Large Analog", "Large Digital", "None"]
			input "roundNumbers", title: "Round Off Decimals", "bool", required: true, defaultValue:true
        }
        
        section("Dropcam Video Streams") {
			paragraph "Enter absolute URL starting with http..."
        	input "dropcamStreamT1", "text", title:"Title 1", required: false
            input "dropcamStreamUrl1", "text", title:"URL 1", required: false
			input "dropcamStreamT2", "text", title:"Title 2", required: false
            input "dropcamStreamUrl2", "text", title:"URL 2", required: false
			input "dropcamStreamT3", "text", title:"Title 3 ", required: false
            input "dropcamStreamUrl3", "text", title:"URL 3", required: false
        }
        
        section("Show Links") {
			paragraph "Enter absolute URL starting with http..."
        	input "linkTitle1", "text", title:"Title 1", required: false
            input "linkUrl1", "text", title:"URL 1", required: false
			input "linkTitle2", "text", title:"Title 2", required: false
            input "linkUrl2", "text", title:"URL 2", required: false
			input "linkTitle3", "text", title:"Title 3", required: false
            input "linkUrl3", "text", title:"URL 3", required: false
        }
	}
	
    page(name: "selectPreferences", title: "Preferences", install: true, uninstall: true) {
        section("Dashboard Preferences...") {
        	label title: "Title", required: false, defaultValue: "ActiON4"
        }
		
        section("Reset AOuth Access Token...") {
        	paragraph "Activating this option will invalidate access token. The new ActiON Dashboard URL will be printed to the logs. Access token will keep resetting until this option is turned off."
        	input "resetOauth", "bool", title: "Reset AOuth Access Token?", defaultValue: false
        }
        
        section("Send text message to...") {
        	paragraph "Optionally, send text message containing the ActiON Dashboard URL to phone number. The URL will be sent in two parts because it's too long."
            input "phone", "phone", title: "Which phone?", required: false
        }
    }
}

mappings {
    path("/ui") {
		action: [
			GET: "html",
		]
	}
    path("/command") {
    	action: [
			GET: "command",
		]
    }
	path("/data") {
    	action: [
			GET: "allDeviceData",
		]
    }
	path("/ping") {
    	action: [
			GET: "ping",
		]
    }
}

def command() {
	log.debug "command received with params $params"
    
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
    }
    
	[status:"ok"]
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
	subscribe(app, getURL)
    scheduledWeatherRefresh()
    getURL(null)
	
	updateStateTS()
	
	subscribe(location, handler)
    subscribe(app, handler)
	subscribe(holiday, "switch.on", handler, [filterEvents: false])
	subscribe(holiday, "switch.off", handler, [filterEvents: false])
	subscribe(holiday, "switch", handler, [filterEvents: false])
	subscribe(holiday, "level", handler, [filterEvents: false])
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
}

def getURL(e) {
    if (resetOauth) {
    	log.debug "Reseting Access Token"
    	state.accessToken = null
    }
    
	if (!state.accessToken) {
    	try {
			createAccessToken()
			log.debug "Creating new Access Token: $state.accessToken"
		} catch (ex) {
			log.error "Did you forget to enable OAuth in SmartApp settings for ActiON Dashboard?"
			log.error ex
		}
    }
	if (state.accessToken) {
		def url1 = "https://graph.api.smartthings.com/api/smartapps/installations/${app.id}/ui"
		def url2 = "?access_token=${state.accessToken}"
		log.info "${title ?: location.name} ActiON Dashboard URL: $url1$url2"
		if (phone) {
			sendSmsMessage(phone, url1)
			sendSmsMessage(phone, url2)
		}
	}
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
<meta name="viewport" content="width=device-width" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<link rel="icon" sizes="192x192" href="https://action-dashboard.github.io/icon.png">
<link rel="apple-touch-icon" href="https://action-dashboard.github.io/icon.png">
<meta name="mobile-web-app-capable" content="yes">
<title>${app.label ?: location.name}</title>

<link rel="stylesheet" href="https://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.css" />
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/weather-icons/1.3.2/css/weather-icons.min.css" />
<link href="https://625alex.github.io/ActiON-Dashboard/style.min.css?v=1" rel="stylesheet">
<link href='https://fonts.googleapis.com/css?family=Mallanna' rel='stylesheet' type='text/css'>

<script src="https://code.jquery.com/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="https://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.js" type="text/javascript"></script>
<script src="https://cdn.jsdelivr.net/coolclock/2.1.4/coolclock.min.js" type="text/javascript"></script>
<script src="https://625alex.github.io/ActiON-Dashboard/script.min.js?v=2" type="text/javascript"></script>

<script>var stateTS = ${getStateTS()};</script>
"""
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
	<div class="icon"><i class="fa fa-comment-o"></i></div>
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
	def data = [tile:"device", active:"inactive", type: "weather", device: device.id]
    ["city", "weather", "feelsLike", "temperature", "localSunrise", "localSunset", "percentPrecip", "humidity", "weatherIcon"].each{data["$it"] = device?.currentValue("$it")}
    data.icon = ["chanceflurries":"wi-snow","chancerain":"wi-rain","chancesleet":"wi-rain-mix","chancesnow":"wi-snow","chancetstorms":"wi-storm-showers","clear":"wi-day-sunny","cloudy":"wi-cloudy","flurries":"wi-snow","fog":"wi-fog","hazy":"wi-dust","mostlycloudy":"wi-cloudy","mostlysunny":"wi-day-sunny","partlycloudy":"wi-day-cloudy","partlysunny":"wi-day-cloudy","rain":"wi-rai","sleet":"wi-rain-mix","snow":"wi-snow","sunny":"wi-day-sunny","tstorms":"wi-storm-showers","nt_chanceflurries":"wi-snow","nt_chancerain":"wi-rain","nt_chancesleet":"wi-rain-mix","nt_chancesnow":"wi-snow","nt_chancetstorms":"wi-storm-showers","nt_clear":"wi-stars","nt_cloudy":"wi-cloudy","nt_flurries":"wi-snow","nt_fog":"wi-fog","nt_hazy":"wi-dust","nt_mostlycloudy":"wi-night-cloudy","nt_mostlysunny":"wi-night-cloudy","nt_partlycloudy":"wi-night-cloudy","nt_partlysunny":"wi-night-cloudy","nt_sleet":"wi-rain-mix","nt_rain":"wi-rain","nt_snow":"wi-snow","nt_sunny":"wi-night-clear","nt_tstorms":"wi-storm-showers","wi-horizon":"wi-horizon"][data.weatherIcon]
	data
}

def renderTile(data) {
	if (data.type == "weather"){
		return """<div class="weather tile w2" data-type="weather" data-device="$data.device" data-weather='${data.encodeAsJSON()}'></div>"""
	} else if (data.tile == "device") {
		return """<div class="$data.type tile $data.active" data-active="$data.active" data-type="$data.type" data-device="$data.device" data-value="$data.value" data-level="$data.level" data-is-value="$data.isValue"><div class="title">$data.name</div></div>"""
	} else if (data.tile == "link") {
		return """<div class="link tile" data-link-i="$data.i"><div class="title">$data.title</div><div class="icon"><a href="$data.link" data-ajax="false" style="color:white"><i class="fa fa-link"></i></a></div></div>"""
	} else if (data.tile == "video") {
		return """<div class="video tile h2 w2" data-link-i="$data.i"><div class="title">$data.title</div><div class="icon" style="margin-top:-82px;"><object width="240" height="164"><param name="movie" value="$data.link"></param><param name="allowFullScreen" value="true"></param><param name="allowscriptaccess" value="always"></param><param name="wmode" value="opaque"></param><embed src="$data.link" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="240" height="164" wmode="opaque"></embed></object></div></div>"""
	} else if (data.tile == "refresh") {
		return """<div class="refresh tile clickable"><div class="title">Refresh</div><div class="footer">Updated $data.ts</div></div>"""
	} else if (data.tile == "mode") {
		return renderModeTile(data)
	} else if (data.tile == "clock") {
		if (data.type == "a") {
			return """<div id="analog-clock" class="clock tile clickable h$data.size w$data.size"><div class="title">$data.date</div><div class="icon" style="margin-top:-${data.size * 45}px;"><canvas id="clockid" class="CoolClock:st:${45 * data.size}"></canvas></div><div class="footer">$data.dow</div></div>"""
		} else {
			return """<div id="digital-clock" class="clock tile clickable w$data.size"><div class="title">$data.date</div><div class="icon ${data.size == 2 ? "" : "text"}" id="clock">*</div><div class="footer">$data.dow</div></div>"""
		}
	} else if (data.tile == "helloHome") {
		return renderHelloHomeTile(data)
	}
	
	return ""
}

def getDeviceData(device, type) {[tile: "device",  active: isActive(device, type), type: type, device: device.id, name: device.displayName, value: getDeviceValue(device, type), level: getDeviceLevel(device, type), isValue: isValue(device, type)]}

def getDeviceFieldMap() {[lock: "lock", holiday: "switch", "switch": "switch", dimmer: "switch", contact: "contact", presence: "presence", temperature: "temperature", humidity: "humidity", motion: "motion", water: "water", power: "power", energy: "energy", battery: "battery"]}

def getActiveDeviceMap() {[lock: "unlocked", holiday: "on", "switch": "on", dimmer: "on", contact: "open", presence: "present", motion: "active", water: "wet"]}

def isValue(device, type) {!(["momentary", "camera"] << getActiveDeviceMap().keySet()).flatten().contains(type)}

def isActive(device, type) {
	def field = getDeviceFieldMap()[type]
	def value = device.respondsTo("currentValue") ? device.currentValue(field) : device.value
	def active = value == getActiveDeviceMap()[type] ? "active" : "inactive"
	active
}

def getDeviceValue(device, type) {
	def unitMap = [temperature: "°", humidity: "%", battery: "%", power: "W", energy: "kWh"]
	def field = getDeviceFieldMap()[type]
	def value = device.respondsTo("currentValue") ? device.currentValue(field) : device.value
	if (!isValue(device, type)) return value
	else return "${roundNumber(value)}${unitMap[type] ?: ""}"
}

def getDeviceLevel(device, type) {if (type == "dimmer" ||  type == "music") return "${device.currentValue("level") / 10.0}".toDouble().round()}

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
	def data = []
	
	if (showClock == "Small Analog") data << [tile: "clock", size: 1, type: "a", date: getDate(), dow: getDOW()]
	else if (showClock == "Large Analog") data << [tile: "clock", size: 2, type: "a", date: getDate(), dow: getDOW()]
    else if (showClock == "Small Digital") data << [tile: "clock", size: 1, type: "d", date: getDate(), dow: getDOW()]
	else if (showClock == "Large Digital") data << [tile: "clock", size: 2, type: "d", date: getDate(), dow: getDOW()]
	
	if (showMode && location.modes) data << [tile: "mode", mode: "$location.mode", isStandardMode: ("$location.mode" == "Home" || "$location.mode" == "Away" || "$location.mode" == "Night"), modes: location.modes.name]
	
	def phrases = location?.helloHome?.getPhrases()*.label?.sort()
	if (showHelloHome && phrases) data << [tile: "helloHome", phrases: phrases]
	
	weather?.each{data << getWeatherData(it)}
	
	holiday?.each{data << getDeviceData(it, "holiday")}
	locks?.each{data << getDeviceData(it, "lock")}
	switches?.each{data << getDeviceData(it, "switch")}
	dimmers?.each{data << getDeviceData(it, "dimmer")}
	momentaries?.each{data << getDeviceData(it, "momentary")}
	contacts?.each{data << getDeviceData(it, "contact")}
	presence?.each{data << getDeviceData(it, "presence")}
	motion?.each{data << getDeviceData(it, "motion")}
	camera?.each{data << getDeviceData(it, "camera")}
	(1..3).each{if (settings["dropcamStreamUrl$it"]) {data << [tile: "video", link: settings["dropcamStreamUrl$it"], title: settings["dropcamStreamT$it"] ?: "Stream $it", i: it]}}
	temperature?.each{data << getDeviceData(it, "temperature")}
	humidity?.each{data << getDeviceData(it, "humidity")}
	water?.each{data << getDeviceData(it, "water")}
	energy?.each{data << getDeviceData(it, "energy")}
	power?.each{data << getDeviceData(it, "power")}
	battery?.each{data << getDeviceData(it, "battery")}
	
	(1..3).each{if (settings["linkUrl$it"]) {data << [tile: "link", link: settings["linkUrl$it"], title: settings["linkTitle$it"] ?: "Link $it", i: it]}}
	
	data << [tile: "refresh", ts: getTS()]
	
	data
}

def html() {render contentType: "text/html", data: "<!DOCTYPE html><html><head>${head()}</head><body style='background-color:black'>\n${renderTiles()}\n${renderWTFCloud()}</body></html>"}

def renderTiles() {"""<div class="tiles">\n${allDeviceData()?.collect{renderTile(it)}.join("\n")}</div>"""}

def renderWTFCloud() {"""<div data-role="popup" id="wtfcloud-popup" data-overlay-theme="b" class="wtfcloud"><div class="icon cloud" onclick="clearWTFCloud()"><i class="fa fa-cloud"></i></div><div class="icon message" onclick="clearWTFCloud()"><i class="fa fa-question"></i><i class="fa fa-exclamation"></i><i class='fa fa-refresh'></i></div></div>"""}
