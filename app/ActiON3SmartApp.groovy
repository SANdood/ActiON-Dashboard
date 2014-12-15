/**
 *  ActiON Dashboard 3.0.4
 *
 *  ActiON Dashboard is a web application to contol and view status of your devices. 
 *  The dashboard is optimized for mobile devices as well as large screens.
 *  Once the dashboard url is generated, it could be used in any modern browser.
 *  There is no need to install SmartThings Mobile application on the device that will run the dashboard.
 *
 *  http://github.com/625alex/ActiON-Dashboard
 *
 *  Donations accepted via PayPal at alex.smart.things@gmail.com
 *
 *  Copyright © 2014 Alex Malikov
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "ActiON Dashboard",
    namespace: "625alex",
    author: "Alex Malikov",
    description: "Self contained web dashboard.",
    category: "Convenience",
    iconUrl: "https://action-dashboard.github.io/ActiON4/web/icon.png",
    iconX2Url: "https://action-dashboard.github.io/ActiON4/web/icon.png",
    oauth: true)


preferences {
	page(name: "selectDevices", title: "Devices", install: false, unintall: true, nextPage: "selectPhrases") {
    
        section("About") {
            paragraph "ActiON Dashboard is a web application dashboard for your devices. \n" +
            "There is no need to install SmartThings Mobile application on devices that will run ActiON Dashboard. \n" +
            "Tap SmartApp icon to print the ActiON Dashboard URL to the logs or SMS number specified in app preferences."
            paragraph "Version 3.0.4\n\n" +
            "http://github.com/625alex/ActiON-Dashboard\n\n" +
            "Donations accepted via PayPal at alex.smart.things@gmail.com. \n" +
            "Copyright © 2014 Alex Malikov"
        }
        
    	section("Allow control of these things...") {
            input "switches", "capability.switch", title: "Which Switches?", multiple: true, required: false
            input "dimmers", "capability.switchLevel", title: "Which Dimmers?", multiple: true, required: false
            input "momentaries", "capability.momentary", title: "Which Momentary Switches?", multiple: true, required: false
            input "locks", "capability.lock", title: "Which Locks?", multiple: true, required: false
        }

        section("View state of these things...") {
            input "contacts", "capability.contactSensor", title: "Which Contact?", multiple: true, required: false
            input "presence", "capability.presenceSensor", title: "Which Presence?", multiple: true, required: false
            input "temperature", "capability.temperatureMeasurement", title: "Which Temperature?", multiple: true, required: false
            input "humidity", "capability.relativeHumidityMeasurement", title: "Which Hygrometer?", multiple: true, required: false
            input "motion", "capability.motionSensor", title: "Which Motion?", multiple: true, required: false
            input "weather", "device.smartweatherStationTile", title: "Which Weather?", multiple: true, required: false
        }
    }
    
    page(name: "selectPreferences", title: "Preferences", install: true, unintall: true) {
        section("Dashboard Preferences...") {
        	label title: "Title", required: false
            input "theme", title: "Theme", "enum", multiple: false, required: true, defaultValue: "Color", options: ["Color", "Black and White", "Grey"]
            input "viewOnly", title: "View Only", "bool", required: true, defaultValue: false
        }
        
        section("Automatically refresh dashboard...") {
        	input "interval", "decimal", title: "Interval (in minutes)", required: true, defaultValue:2
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
    
    page(name: "selectPhrases", title: "Hello Home", content: "selectPhrases")
}


def selectPhrases() {
	def phrases = location?.helloHome?.getPhrases()*.label
    phrases?.sort()
    log.debug "phrases: $phrases"
    
    return dynamicPage(name: "selectPhrases", title: "Other Tiles", install: false, uninstall: true, nextPage: "selectPreferences") {
        if (phrases) {
            section("Hello, Home!") {
                input "showHelloHome", title: "Show Hello, Home! Phrases", "bool", required: true, defaultValue: true
                input "phrases", "enum", title: "Which phrases?", multiple: true, options: phrases, required: false
            }
        }
        
        section("Show...") {
        	input "showMode", title: "Show Mode", "bool", required: true, defaultValue: true
            input "showClock", title: "Show Clock", "enum", multiple: false, required: true, defaultValue: "Digital", options: ["Digital", "Analog", "None"]
        }
        
        section("Show Link 1...") {
        	input "link1title", "text", title:"Link 1 Title", required: false
            input "link1url", "text", title:"Link 1 URL", required: false
        }
        
        section("Show Link 2...") {
        	input "link2title", "text", title:"Link 2 Title", required: false
            input "link2url", "text", title:"Link 2 URL", required: false
        }
        
        section("Show Link 3...") {
        	input "link3title", "text", title:"Link 3 Title", required: false
            input "link3url", "text", title:"Link 3 URL", required: false
        }
    }
}

mappings {
    path("/data") {
		action: [
			GET: "list",
		]
	}
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
}

def command() {
	if (viewOnly) {
		return false;
	}
	
	log.debug "command received with params $params"
    
    def id = params.id
    def type = params.type
    def value = params.value
    
    def device
    def endState
    def attribute
    
    if (value == "toggle" && (type == "dimmer" || type == "switch")) {
    	device = (type == "dimmer" ? dimmers : switches)?.find{it.id == id}
        attribute = "switch"
        
        log.debug "command toggle for dimmer/switch $device"
        if (device) {
            if(value == "toggle") {
                if(device.currentValue('switch') == "on") {
                    device.off()
                    endState = "off"
                } else {
                    device.on()
                    endState = "on"
                }
            } else if (value == "on") {
                device.on()
                endState = "on"
            } else if (value == "off") {
                device.off()
                endState = "off"
            }
        }
    } else if (type == "dimmer" && value == "0") {
    	device = dimmers?.find{it.id == id}
        attribute = "switch"
        endState = "off"
        
        if (device) {
        	device.setLevel(0)
        	device.off()
        }
    } else if (type == "dimmer") {
    	device = dimmers?.find{it.id == id}
        attribute = "level"
        endState = Math.min(value as Integer, 99) as String
        
        if (device) {
        	device.setLevel(Math.min(value as Integer, 99))
        }
    } else if (type == "lock") {
    	device = locks?.find{it.id == id}
        attribute = "lock"
        
        if (device) {
        	log.debug "current lock status ${device.currentValue('lock')}"
        	if(device.currentValue('lock') == "locked") {
                device.unlock()
                endState = "unlocked"
            } else {
                device.lock()
                endState = "locked"
            }
            
        }
    } else if (type == "mode") {
		setLocationMode(value)
	} else if (type == "helloHome") {
    	device = "helloHome"
        log.debug "executing Hello Home '$value'"
    	location.helloHome.execute(value)
    } else if (type == "momentary") {
    	device = momentaries?.find{it.id == id}
        if (device) {
        	device.push()
        }
    }
    
    def isUpdated = waitForUpdate(type, device, endState, attribute)
    
    def response = [:]
    
    if (isUpdated) {
    	response.status = "ok"
    } else {
    	response.status = "refresh"
    }
    log.debug "isUpdated for $device : $isUpdated"
    
    render contentType: "application/javascript", data: "${params.callback}(${response.encodeAsJSON()})"
}

/*
	Hacked varsion of long poll. Will wait up to 15 seconds for the status to update. If times out without
    ever updating, the page will be forsed to refresh right away.
*/
def waitForUpdate(type, device, endState, attribute) {
	if (type == "mode" || type == "helloHome" || type == "momentary") return true
    
	log.debug "about to check $device attribute $attribute for $endState"
	if (device && endState && attribute) {
    	for (def i = 0; i < 5 ; i++ ) {
            if (device.currentValue(attribute)?.toString() == endState) {
            	return true
            } else {
            	log.debug "checking #$i, expected $device attribute $attribute to be $endState but was ${device.currentValue(attribute)}"
            	pause(3000)
            }
        }
    }
    return false
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
}

def getURL(e) {
    if (resetOauth) {
    	log.debug "Reseting Access Token"
    	state.accessToken = null
    }
    
	if (!state.accessToken) {
		try {
			createAccessToken()
		} catch (Exception ex) {
			log.debug "Did you forget to enable OAuth in SmartApp settings for ActiON Dashboard?"
			log.debug ex
		}
        log.debug "Creating new Access Token: $state.accessToken"
    }
	
	def url1 = "https://graph.api.smartthings.com/api/smartapps/installations/${app.id}/ui"
    def url2 = "?access_token=${state.accessToken}"
    log.debug "${title ?: location.name} ActiON Dashboard URL: $url1$url2"
    if (phone) {
        sendSmsMessage(phone, url1)
        sendSmsMessage(phone, url2)
    }
}


def scheduledWeatherRefresh() {
    runIn(3600, scheduledWeatherRefresh, [overwrite: false])
	weather?.refresh()
    state.lastWeatherRefresh = getTS()
}

def index() {
	["index", "list", "html"]
}

def list() {
	render contentType: "application/javascript", data: "${params.callback}(${data().encodeAsJSON()}})"
}

def data() {
    [
    	locks: locks?.collect{[type: "lock", id: it.id, name: it.displayName, status: it.currentValue('lock') == "locked" ? "locked" : "unlocked"]}?.sort{it.name},
        switches: switches?.collect{[type: "switch", id: it.id, name: it.displayName, status: it.currentValue('switch')]}?.sort{it.name},
        dimmers: dimmers?.collect{[type: "dimmer", id: it.id, name: it.displayName, status: it.currentValue('switch'), level: it.currentValue('level')]}?.sort{it.name},
        momentary: momentaries?.collect{[type: "momentary", id: it.id, name: it.displayName]}?.sort{it.name},
        contacts: contacts?.collect{[type: "contact", id: it.id, name: it.displayName, status: it.currentValue('contact')]}?.sort{it.name},
        presence: presence?.collect{[type: "presence", id: it.id, name: it.displayName, status: it.currentValue('presence')]}?.sort{it.name},
        motion: motion?.collect{[type: "motion", id: it.id, name: it.displayName, status: it.currentValue('motion')]}?.sort{it.name},
        temperature: temperature?.collect{[type: "temperature", id: it.id, name: it.displayName, status: roundNumber(it.currentValue('temperature'), "°")]}?.sort{it.name},
        humidity: humidity?.collect{[type: "humidity", id: it.id, name: it.displayName, status: roundNumber(it.currentValue('humidity'), "%")]}?.sort{it.name},
    ]
}

def roundNumber(num, unit) {
	if (num == null || num == "") return "n/a"
	if (!"$num".isNumber()) return num
	else {
    	try {
            return Math.round("$num".toDouble()) + (unit ?: "")
        } catch (e) {
        	return num
        }
    }
}

def html() {
    render contentType: "text/html", data: "<!DOCTYPE html><html><head>${head()}</head><body>${body()}\n${script()}</body></html>"
}

def head() {
	"""
    <meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	<link rel="icon" sizes="192x192" href="https://action-dashboard.github.io/ActiON4/web/icon.png">
	<link rel="apple-touch-icon" href="https://action-dashboard.github.io/ActiON4/web/icon.png">
	<meta name="mobile-web-app-capable" content="yes">
    <title>${app.label ?: location.name}</title>
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="//code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.css" />
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/weather-icons/1.2/css/weather-icons.min.css" />
    <script src="//code.jquery.com/jquery-2.1.1.min.js"></script>
    <script src="//code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.js"></script>
    <script src="//cdn.jsdelivr.net/coolclock/2.1.4/coolclock.min.js"></script>
    
    ${style()}
    ${themes()}
    ${media()}
    """
}                                                              

def script() {
"""
<script>
	\$(function() {
        \$("body").bind("contextmenu",function(){
           return false;
        });
        
    	\$(".lock, .switch").click(function() {
			${viewOnly ? "return false;" : ""}
            animateToggle(\$(this));
            sendCommand(\$(this).attr("deviceType"), \$(this).attr("deviceId"), "toggle");
		});
        
        \$(".momentary").click(function() {
			${viewOnly ? "return false;" : ""}
            animateClick(\$(this));
            sendCommand(\$(this).attr("deviceType"), \$(this).attr("deviceId"), "toggle");
		});
        
        \$(".link").click(function() {
            animateClick(\$(this));
		});
        
        \$(".dimmer").on( 'slidestop', function( e ) {
        	//animateSliderToOff(\$(this).closest(".st-tile"));
            var val = \$(this).find("input").val();
            if (val == 0) {
            	animateDimmer(\$(this).closest(".st-tile"), "fa fa-toggle-off");
            } else {
                animateDimmer(\$(this).closest(".st-tile"), "fa fa-toggle-on");
            }
            sendCommand("dimmer", \$(this).find("input").attr("deviceId"), \$(this).find("input").val());
        });
        
        \$(".dimmer .st-icon").click(function() {
            animateToggle(\$(this).closest(".st-tile"));
            sendCommand("dimmer", \$(this).closest(".st-tile").attr("deviceId"), "toggle");
            var status = \$(this).closest(".st-tile").attr("deviceStatus");
            if (status == "off") {
            	var level = \$(this).closest(".st-tile").find("input").attr("deviceLevel");
            	\$(this).closest(".st-tile").attr("deviceStatus", "on");
                \$(this).closest(".st-tile").find("input").val(level).slider("refresh");
            } else {
            	\$(this).closest(".st-tile").find("input").val(0).slider("refresh");
                \$(this).closest(".st-tile").attr("deviceStatus", "off");
            }
            
		});
        
        \$(".refresh, .clock").click(function() {
        	animateClick(\$(this));
            refresh();
		});
        
        \$("#st-modes li").click(function() {
        	\$("#st-modes").popup("close");
        	animateClick(\$(".mode"));
            var mode = \$(this).text();
			sendCommand("mode", "mode", mode);
            
            \$("#mode-name").html(mode).hide();
            \$("#mode-icon i").hide();
            
            if (mode == "Home") {
            	\$("#mode-home").show();
            } else if (mode == "Away") {
            	\$("#mode-away").show();
            } else if (mode == "Night") {
            	\$("#mode-night").show();
            } else {
            	\$("#mode-name").show();
            }
            return false;
        });
        
        \$("#mode_mode").click(function() {
        	\$("#st-modes").popup("open", {positionTo: \$(this)});
        });
        
        \$("#st-phrases li").on("click", function() {
        	\$("#st-phrases").popup("close");
        	animateClick(\$(".hello-home"));
			sendCommand("helloHome", "helloHome", \$(this).text());
            return false;
        });
        
        \$("#helloHome_helloHome").click(function() {
        	\$("#st-phrases").popup("open", {positionTo: \$(this)});
        });
        
        animateMotion();
    
	});
    
    function animateMotion() {
    	\$(".st-animate-motion").animate({opacity: 0.3}, 2000, "swing").animate({opacity: 0.3}, 500).animate({opacity: 1}, 1000, "swing").animate({opacity: 1}, 500, animateMotion);
    }
    
    function animateToggle(element) {
        var oldIcon = element.find(".st-icon i").attr("class");
        var newIcon = toggleIcon(oldIcon);
        spinOn(element).animate({opacity: .5}, 300).find(".st-icon i").removeClass(oldIcon).addClass(newIcon).closest(element).animate({opacity: 1}, 200);
        return element;
    }
    
    function animateDimmer(element, newIcon) {
        var oldIcon = element.find(".st-icon i").attr("class");
        spinOn(element).animate({opacity: .5}, 300).find(".st-icon i").removeClass(oldIcon).addClass(newIcon).closest(element).animate({opacity: 1}, 200);
        return element;
    }
    
    function animateClick(element) {
        spinOn(element).animate({opacity: .5}, 300).animate({opacity: 1}, 200);
    }
    
    function sendCommand(type, id, value) {
    	//alert("&type=" + type + "&id=" + id + "&value=" + value);
        
        var url = window.location.href.slice(window.location.href.indexOf('?') + 1);
        var hashIndex = url.indexOf('#');
        if (hashIndex > 0) {
        	url = url.substring(0, hashIndex);
        }
        var url = "command/?" + url + "&type=" + type + "&id=" + id + "&value=" + value;
        
		\$.getJSON(url + "&callback=?")
            .done(function( data ) {
            	if (data.status == "ok") {
                	var elementId = "#" + type + "_" + id;
                    spinOff(\$(elementId));
                    //alert("ok");
                    refresh(30);
                } else if (data.status == "refresh") {
                	//alert("refresh");
                	refresh(5);
                }
            }).fail(function( jqxhr, textStatus, error ) {
                //alert("error");
                refresh(10);
            });
    }
    
   	function refresh(timeout) {
    	if (!timeout) {
        	setTimeout(function() { doRefresh() }, 100);
        } else {
        	setTimeout(function() { doRefresh() }, timeout * 1000);
        }
    }
    
    function doRefresh() {
    	\$(".refresh .st-icon").addClass("fa-spin");
        location.reload();
    }
    
    function spinOn(element) {
    	element.closest(".st-tile").find(".spin").animate({opacity: .5}, 500);
        return element;
    }
    
    function spinOff(element) {
    	element.closest(".st-tile").find(".spin").animate({opacity: 0}, 1000);
        return element;
    }
    
    function toggleIcon(icon) {
    	var icons = {
        "fa fa-toggle-off" : "fa fa-toggle-on",
        "fa fa-toggle-on" : "fa fa-toggle-off",
        "fa fa-lock" : "fa fa-unlock-alt",
        "fa fa-unlock-alt" : "fa fa-lock"
        }
        
        return icons[icon];
    }
    
    refresh(60 * $interval)
    
</script>
"""
}

def style() {
"""
<style type="text/css">
.refresh, .clock {
	cursor: pointer;
}

.lock, .switch, .dimmer, .momentary {
	cursor: ${viewOnly ? "default" : "pointer"};
}

.st-title {
	color: white;
    font-size: 14px;
    line-height: 16px;
    font-weight:400;
    font-style: normal;
    text-shadow: none;
}

.st-tile {
	float: left;
   	position: relative;
   	width: 10%;
   	padding-bottom: 10%;
    cursor:defaut!important;
}

.size2x1 {
	width:20%;
}

.st-container {
   overflow: hidden;
   margin: 5px;
}

.st-icon {
    color: white;
    height: 3em;
    width: 3em;
    left: 50%;
    top: 50%;
    margin-left: -1.5em;
    margin-top: -1.5em;
    position: absolute;
    text-align: center;
}

.fa, .wi {
	font-size:3em;
}

.st-tile-content {
   position: absolute;
   left: 5px;
   right: 5px;
   top: 5px;
   bottom: 5px;
   overflow: hidden;
   background-color: grey;
   padding: 5px;
}

.x2 {
	width: 20%;
	padding-bottom: 10%;
}

.clock .st-tile-content {
	background-color: #6D8764;
}

.presence .st-tile-content {
	background-color: #2d89ef;
}

.lock .st-tile-content {
	background-color: #da532c;
}

.hello-home .st-tile-content {
	background-color: #76608A;
    cursor: pointer;
}

.switch .st-tile-content {
	background-color: #99b433;
}

.dimmer .st-tile-content {
	/*background-color: #1e7145;*/
    
    background-color: #3C9840;
}

.contact .st-tile-content {
	background-color: #ce7898;
}

.refresh .st-tile-content {
	background-color: #647687;
}

.temperature .st-tile-content {
	background-color: #e3a21a;
}

.mode .st-tile-content {
	background-color: #603cba;
    cursor:pointer;
}

.motion .st-tile-content {
	background-color: #0050EF;
}

.humidity .st-tile-content {
	background-color: #87794e;
}

.momentary .st-tile-content {
	background-color: #009065
}

.link .st-tile-content {
	background-color: #263B44;
}

.r45 {
    -moz-transform:rotate(45deg);
    -webkit-transform:rotate(45deg);
    -o-transform:rotate(45deg);
    -ms-transform:rotate(45deg);
    transform:rotate(45deg)
}

#clock {
	height: 1em;
    font-size:2em;
    line-height:1em;
    margin-top:-0.5em;
}

.st-container {
	margin: 0;
    padding: 0;
    padding-bottom: 5px;
    background-color: #1d1d1d!important;
}

.ui-content {
	padding:5px 0 0 5px;;
}

div.st-tile-content {
	#background-color:lightGrey!important;
}

.opaque {
	opacity: 0.3;
	-ms-filter:"progid:DXImageTransform.Microsoft.Alpha(Opacity=30)";
	filter: alpha(opacity=30);
}

* {
	text-shadow:none!important;
    font-family: Arial, Verdana, Gadget, "Segoe UI Light_","Open Sans Light",Verdana,Arial,Helvetica,sans-serif;
}

.temperature .st-icon, .humidity .st-icon {
	font-size:3em;
    left:0;
    margin:0;
    width:100%;
    height:100%;
    top:25%;
}

.spin {
	color: white;
	/*visibility: hidden;*/
    margin-right:3px;
    opacity: 0;
	-ms-filter:"progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";
	filter: alpha(opacity=0);
    font-size:10px!important;
    position:absolute;
    bottom:7px;
    right:7px;
}

.ui-slider-track.ui-mini {
	margin-left:0;
    width:100%;
}

.ui-slider-popup {
	height:12px;
    width:20px;
    padding-top:0px;
    padding-bottom:3px;
    font-size:12px;
}

.ui-slider-handle {
	background-color:white;
}

.full-width-slider input {
    display: none;
}

.full-width-slider {
	/*width:100px;
    height:50px;
    left:50%;
    top:50%;
    margin-left:-50px;
    margin-top:20px;
    position: absolute;*/
    width:80%;
    height:50px;
    left:50%;
    top:50%;
    margin-left:-40%;
    margin-top:10%;
    position: absolute;
    
}
.full-width-select {
	width:100px;
    height:50px;
    left:50%;
    top:50%;
    margin-left:-50px;
    margin-top:-25px;
    position: absolute;
    
}

.full-width-select .ui-btn {
	background:none;
    color: white
}

.ui-btn, .hh-heading {
	text-align:center!important;
}

.toggle {
	display:none;
}

.ui-btn-corner-all, .ui-btn.ui-corner-all, .ui-slider-track.ui-corner-all, .ui-flipswitch.ui-corner-all, .ui-li-count {
    border-radius: 1em!important;
}

.ui-slider-track.ui-mini .ui-slider-handle {
	height:20px;
    width:20px;
    margin:-11px 0 0 -11px;
}

.footer {
	position: absolute; 
    bottom:7px; left:7px;
    font-size:10px;
    color: white;
}

.st-icon.st-animate-motion {
	height:1em;
    margin-top:-0.75em;
    margin-left:-0.5em;
    width:1em;
    font-size:1em;
}

.footer .wi {
	font-size: 20px;
}

</style>
"""
}

def themes() {
	if (theme == "Black and White") {
"""
<style>
.st-tile-content {
	background-color: black!important;
}
body {
	background-color: lightGrey!important;
}

.ui-slider-bg {
	background-color:white!important;
}
</style>
"""
	} else if (theme == "Grey") {
"""
<style>
body {
	background-color: #1d1d1d!important;
}
.st-tile-content {
	background-color: grey!important;
}

.ui-slider-bg {
	background-color:white!important;
}
</style>
"""
    } else {
"""
<style>
body {
	background-color: #1d1d1d!important;
}

.ui-slider-bg {
	background-color:white!important;
}

</style>
"""
    }
}

def media() {
"""
<style>
    @media only screen and (max-width : 480px) {
   /* Smartphone view: 3 tile */
   .st-tile {
      width: 33.3%;
      padding-bottom: 33.3%;
   }
   
   .size2x1 {
      width: 66.6%;
   }
   
   .st-tile-content {
	  left:2px;
      right:2px;
      top:2px;
      bottom:2px;
   }
   .ui-content {
   	  padding:0;
   }
}

@media only screen and (max-width : 565px) and (min-width : 481px) {
   /* Tablet view: 4 tiles */
   .st-tile {
      width: 25%;
      padding-bottom: 25%;
   }
   
   .size2x1 {
      width: 50%;
   }
   
   .st-tile-content {
	  left:2px;
      right:2px;
      top:2px;
      bottom:2px;
   }
   .ui-content {
   	  padding:0px;
   }
}

@media only screen and (max-width : 650px) and (min-width : 566px) {
   /* Tablet view: 5 tiles */
   .st-tile {
      width: 20%;
      padding-bottom: 20%;
   }
   
   .size2x1 {
      width: 40%;
   }
   
   .st-tile-content {
	  left:2px;
      right:2px;
      top:2px;
      bottom:2px;
   }
   .ui-content {
   	  padding:0px;
   }
}


@media only screen and (max-width : 850px) and (min-width : 651px) {
   /* Small desktop / ipad view: 6 tiles */
   .st-tile {
      width: 16.6%;
      padding-bottom: 16.6%;
   }
   
   .size2x1 {
      width: 33.2%;
   }
}

@media only screen and (max-width : 1050px) and (min-width : 851px) {
   /* Small desktop / ipad view: 7 tiles */
   .st-tile {
      width: 14.2%;
      padding-bottom: 14.2%;
   }
   
   .size2x1 {
      width: 28.4%;
   }
}

@media only screen and (max-width : 1290px) and (min-width : 1051px) {
   /* Medium desktop: 8 tiles */
   .st-tile {
      width: 12.5%;
      padding-bottom: 12.5%;
   }
   .size2x1 {
      width: 25%;
   }
}
</style>
"""
}

def renderClock() {
	if (showClock == "Analog") renderAnalogClock()
    else if (showClock == "Digital") renderDigitalClock()
	else return ""
}

def renderAnalogClock() {
"""
<div id="analog-clock" class="st-tile clock">
	<div class="st-tile-content">
    	<div class="st-title" id="date">
        	${getDate()}
        </div>
        <div class="st-icon" style="width:80px; height:80px; margin-left:-40px;margin-top:-40px;">
        	<canvas id="clockid" class="CoolClock:st:40"></canvas>
        </div>
        <div class="footer">${getDOW()}</div>
	</div>
</div>
<script>
    CoolClock.config.skins = { st: {
outerBorder: { lineWidth: 2, radius: 80, color: "white", alpha: 0 },
smallIndicator: { lineWidth: 5, startAt: 88, endAt: 94, color: "yellow", alpha: 0 },
largeIndicator: { lineWidth: 5, startAt: 90, endAt: 94, color: "white", alpha: 1 },
hourHand: { lineWidth: 8, startAt: 0, endAt: 60, color: "white", alpha: 1 },
minuteHand: { lineWidth: 8, startAt: 0, endAt: 80, color: "white", alpha: 1 },
secondHand: { lineWidth: 5, startAt: 89, endAt: 94, color: "white", alpha: 1 },
secondDecoration: { lineWidth: 3, startAt: 0, radius: 4, fillColor: "black", color: "black", alpha: 0 }
    }};
</script>
"""
}

def renderDigitalClock() {
"""
<div id="digital-clock" class="st-tile clock">
	<div class="st-tile-content">
    	<div class="st-title" id="date">
        	${getDate()}
        </div>
        <div class="st-icon" id="clock">
        	*
        </div>
        <div class="footer">${getDOW()}</div>
	</div>
</div>
<script>
function startTime() {
    var today=new Date();
    var h=today.getHours();
    if (h > 12) {
    	h = h - 12;
    }
    var m=today.getMinutes();
    var s=today.getSeconds();
    m = checkTime(m);
    s = checkTime(s);
    document.getElementById('clock').innerHTML = h+":"+m;
    setTimeout(function(){startTime()},500);
}

function checkTime(i) {
    if (i<10) {i = "0" + i};  // add zero in front of numbers < 10
    return i;
}
startTime();
</script>
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

def renderRefresh() {
"""
<div id="refresh" class="st-tile refresh" deviceId="refresh" deviceType="refresh">
	<div class="st-tile-content">
    	<div class="st-title">
        	Refresh
        </div>
        <div class="st-icon">
        	<i class="fa fa-refresh"></i>
        </div>
        <div class="footer">Updated ${getTS()}</div>
	</div>
</div>
"""
}

def renderMode() {
	if (!showMode) return ""
    
    def mode = location.mode.toString()
    
    def modeList = ""
    location.modes?.each{modeList = modeList + """<li data-icon="false"><a href="#" class="st-hello-home-button">$it<i class="spin fa fa-refresh fa-spin"></i></a></li>"""}
    
    def modes = """<option value="$location.mode" selected="selected">$location.mode</option>"""

    location.modes.each {
        if (mode != it.toString()) {
            modes = modes + """<option value="$it">$it</option>\n"""
        }
    }
    
"""
<div id="mode_mode" class="st-tile mode">
	<div class="st-tile-content">
    	<div class="st-title">
        	Mode
        </div>
        <div data-role="popup" id="st-modes">
        	<ul data-role="listview" data-inset="true" style="min-width:210px;">
            	$modeList
            </ul>
        </div>
        <div class="st-icon" id="mode-icon">
			<i class="fa fa-home" style="${mode != "Home" ? "display:none" : ""}" id="mode-home"></i>
            <i class="fa fa-moon-o" style="${mode != "Night" ? "display:none" : ""}" id="mode-night"></i>
            <i class="fa fa-sign-out" style="${mode != "Away" ? "display:none" : ""}" id="mode-away"></i>
        </div>
        
        <div class="st-icon" style="${mode in ["Home", "Away", "Night"] ? "display:none;": ""} font-size:1.25em; height:2em;line-height:2em; margin-top:-1em;" id="mode-name">$location.mode</div>
        <i class="spin fa fa-refresh fa-spin"></i>
	</div>
</div>
"""
}

def renderHelloHome() {
	if (!phrases || !showHelloHome) return ""
    
    def phraseList = ""
    phrases?.each{phraseList = phraseList + """<li data-icon="false"><a href="#" class="st-hello-home-button">$it</a></li>"""}

"""
<div id="helloHome_helloHome" class="st-tile hello-home" data-rel="popup">
	<div class="st-tile-content">
    	<div class="st-title">
        	Hello, Home!
        </div>
        <div class="st-icon">
			<i class="fa fa-comment-o"></i>
        </div>
        <div data-role="popup" id="st-phrases">
            <ul data-role="listview" data-inset="true" style="min-width:210px;">
                $phraseList
            </ul>
		</div>
        <i class="spin fa fa-refresh fa-spin"></i>
	</div>
</div>
"""
}

def renderTemperature(device) {
"""
<div id="temperature_$device.id" class="st-tile temperature">
	<div class="st-tile-content">
    	<div class="st-title">
        	$device.name
        </div>
        <div class="st-icon">
        	$device.status
        </div>
        <div class="footer"><i class="wi wi-thermometer"></i></div>
	</div>
</div>
"""
}

def renderHumidity(device) {
"""
<div id="dhumidity_$device.id" class="st-tile humidity">
	<div class="st-tile-content">
    	<div class="st-title">
        	$device.name
        </div>
        <div class="st-icon">
        	$device.status
        </div>
        <div class="footer"><i class="wi wi-sprinkles"></i></div>
	</div>
</div>
"""
}

def renderDimmer(device) {
"""
<div id="dimmer_$device.id" class="st-tile $device.type" 
	deviceId="$device.id" deviceType="dimmer" deviceLevel="$device.level" deviceStatus="$device.status">
	<div class="st-tile-content">
    	<div class="st-title">
        	$device.name
        </div>
        <div class="st-icon">
        	<i class="fa ${device.status == "on" ? "fa-toggle-on" : "fa-toggle-off"}"></i>
        </div>
        <div class="full-width-slider" style="${viewOnly ? "display:none" : ""}">
            <label for="dimmer_$device.id" class="ui-hidden-accessible">Level:</label>
            <input name="dimmer_$device.id" id="dimmer_$device.id" min="0" max="100" value="${device.status == "on" ? device.level : 0}" type="range" 
            deviceLevel="$device.level"
            data-show-value="false" data-mini="true" data-popup-enabled="true" 
            data-disabled="$viewOnly"
            data-highlight="true" step="5" deviceId="$device.id">
		</div>
        <i class="spin fa fa-refresh fa-spin"></i>
	</div>
</div>
"""
}

def renderSwitch(device) {
"""
<div id="switch_$device.id" class="st-tile switch" deviceId="$device.id" deviceType="switch">
	<div class="st-tile-content">
    	<div class="st-title">
        	$device.name
        </div>
        <div class="st-icon">
        	<i class="fa ${device.status == "on" ? "fa-toggle-on" : "fa-toggle-off"}"></i>
        </div>
        <i class="spin fa fa-refresh fa-spin"></i>
	</div>
</div>
"""
}


def renderLock(device) {
"""
<div id="lock_$device.id" class="st-tile lock" deviceId="$device.id" deviceType="lock">
	<div class="st-tile-content">
    	<div class="st-title">
            $device.name
        </div>
        <div class="st-icon">
        	<i class="fa ${device.status == "locked" ? "fa-lock" : "fa-unlock-alt"}"></i>
        </div>
        <i class="spin fa fa-refresh fa-spin"></i>
	</div>
</div>
"""
}

def renderPresence(device) {
"""
<div id="presence_$device.id" class="st-tile presence">
	<div class="st-tile-content">
    	<div class="st-title">
        	$device.name
        </div>
        <div class="st-icon">
        <i class="fa ${device.status == "present" ? "fa-map-marker" : "opaque fa-map-marker"}"></i>
        </div>
	</div>
</div>
"""
}

def renderContact(device) {
"""
<div id="contact_$device.id" class="st-tile contact">
	<div class="st-tile-content">
    	<div class="st-title">
        	$device.name
        </div>
        <div class="st-icon">
        	<i class="r45 fa ${device.status == "open" ? "fa-expand" : "fa-compress"}"></i>
        </div>
	</div>
</div>
"""
}


def renderWeather() {
	if (!weather) return ""
    
    def allWeatherTiles = ""
    
    weather.each {
    	allWeatherTiles = allWeatherTiles + renderWeather(it)
    }
	
    allWeatherTiles
}

def renderWeather(device) {
	def weatherIcons = [
        "chanceflurries" :		"wi-snow",
        "chancerain" :			"wi-rain",
        "chancesleet" :			"wi-rain-mix",
        "chancesnow" :			"wi-snow",
        "chancetstorms" :		"wi-storm-showers",
        "clear" :				"wi-day-sunny",
        "cloudy" :				"wi-cloudy",
        "flurries" :			"wi-snow",
        "fog" :					"wi-fog",
        "hazy" :				"wi-dust",
        "mostlycloudy" :		"wi-cloudy",
        "mostlysunny" :			"wi-day-sunny",
        "partlycloudy" :		"wi-day-cloudy",
        "partlysunny" :			"wi-day-cloudy",
        "rain" :				"wi-rai",
        "sleet" :				"wi-rain-mix",
        "snow" :				"wi-snow",
        "sunny" :				"wi-day-sunny",
        "tstorms" :				"wi-storm-showers",
        "nt_chanceflurries" :	"wi-snow",
        "nt_chancerain" :		"wi-rain",
        "nt_chancesleet" :		"wi-rain-mix",
        "nt_chancesnow" :		"wi-snow",
        "nt_chancetstorms" :	"wi-storm-showers",
        "nt_clear" :			"wi-stars",
        "nt_cloudy" :			"wi-cloudy",
        "nt_flurries" :			"wi-snow",
        "nt_fog" :				"wi-fog",
        "nt_hazy" :				"wi-dust",
        "nt_mostlycloudy" :		"wi-night-cloudy",
        "nt_mostlysunny" :		"wi-night-cloudy",
        "nt_partlycloudy" :		"wi-night-cloudy",
        "nt_partlysunny" :		"wi-night-cloudy",
        "nt_sleet" :			"wi-rain-mix",
        "nt_rain" :				"wi-rain",
        "nt_snow" :				"wi-snow",
        "nt_sunny" :			"wi-night-clear",
        "nt_tstorms" :			"wi-storm-showers",
        "wi-horizon" :			"wi-horizon"
        ]
    def wm = [:]
    ["city", "weather", "feelsLike", "temperature", "localSunrise", "localSunset", "percentPrecip", "humidity", "weatherIcon"].each{
        wm["$it"] = device?.currentValue("$it")
    }
    def icon = weatherIcons[wm.weatherIcon]
    
"""
<div id="weather" class="st-tile size2x1 weather">
	<div class="st-tile-content" style="background-color: #A20025;">
    	<div class="st-title">$wm.city<br/><span class="mini-font">$wm.weather, feels like $wm.feelsLike°<span></div>
        <div class="st-icon" style="width:12em; margin-left: -6em;line-height:3em;">
            <span style="font-size:3em">$wm.temperature°</span>
            <i class="wi $icon" style="font-size:2.3em"></i>
        </div>
     	<div class="footer">$wm.localSunrise <i class="wi wi-horizon"></i> $wm.localSunset</div>
        <div class="footer" style="right:7px; text-align: right">precipitation $wm.percentPrecip%<br/>humidity $wm.humidity%</div>
	</div>
</div>
"""
}

def renderMotion(device) {
"""
<div id="motion_$device.id" class="st-tile motion">
	<div class="st-tile-content">
    	<div class="st-title">
        	$device.name
        </div>
        <div class="st-icon">
        	<i class="fa fa-square-o"></i>
        </div>
        ${device.status == "active" ? """<div class="st-icon st-animate-motion"><i class="fa fa-user" style="font-size:1em;"></i></div>""" : ""}
	</div>
</div>
"""
}

def renderMomentary(device) {
"""
<div id="momentary_$device.id" class="st-tile momentary" deviceId="$device.id" deviceType="momentary">
	<div class="st-tile-content">
    	<div class="st-title">
        	$device.name
        </div>
        <div class="st-icon">
        	<i class="fa fa-circle-o"></i>
        </div>
        <i class="spin fa fa-refresh fa-spin"></i>
	</div>
</div>
"""
}

def renderLink(i) {
	def link = null
    def title
    if (i == 1) {
    	link = link1url
        title = link1title
    } else if (i == 2) {
    	link = link2url
        title = link2title
    } else if (i == 3) {
    	link = link3url
        title = link3title
    } else {
    	return ""
    }
    
    if (!link) {
    	return ""
	}
    
    if (!title || title == "") {
    	title = "Link $i"
    }
    
"""
<div class="st-tile link" deviceType="link">
	<div class="st-tile-content">
    	<div class="st-title">
        	$title
        </div>
        <div class="st-icon">
        	<a href="$link" title="$title" style="color: white" data-ajax="false">
        		<i class="fa fa-link"></i>
            </a>
        </div>
        <i class="spin fa fa-refresh fa-spin"></i>
	</div>
</div>
"""
}

def renderDevices() {
	def devices = ""
    data().values().flatten().each{devices = devices + renderDevice(it)}
    devices
}

def renderDevice(device) {
    if (!device) return ""
    if (device.type == "dimmer") return renderDimmer(device)
    if (device.type == "switch") return renderSwitch(device)
    if (device.type == "lock") return renderLock(device)
    if (device.type == "presence") return renderPresence(device)
    if (device.type == "contact") return renderContact(device)
    if (device.type == "temperature") return renderTemperature(device)
    if (device.type == "motion") return renderMotion(device)
    if (device.type == "humidity") return renderHumidity(device)
    if (device.type == "momentary") return renderMomentary(device)
    else return ""
    
}

def body() {
    
	"""
    <div class="st-container" data-role="page">
    	<div data-role="content" data-theme="c">
            ${renderClock()}
            ${renderWeather()}
            ${renderMode()}
            ${renderHelloHome()}
            ${renderDevices()}
            ${renderLink(1)}
            ${renderLink(2)}
            ${renderLink(3)}
            ${renderRefresh()}
		</div>
    </div>
    """
}
