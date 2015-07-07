var scriptVersion = "5.4.1";

/*CoolClock*/
window.CoolClock=function(options){return this.init(options)};CoolClock.config={tickDelay:1000,longTickDelay:15000,defaultRadius:85,renderRadius:100,defaultSkin:"chunkySwiss",showSecs:true,showAmPm:true,skins:{swissRail:{outerBorder:{lineWidth:2,radius:95,color:"black",alpha:1},smallIndicator:{lineWidth:2,startAt:88,endAt:92,color:"black",alpha:1},largeIndicator:{lineWidth:4,startAt:79,endAt:92,color:"black",alpha:1},hourHand:{lineWidth:8,startAt:-15,endAt:50,color:"black",alpha:1},minuteHand:{lineWidth:7,startAt:-15,endAt:75,color:"black",alpha:1},secondHand:{lineWidth:1,startAt:-20,endAt:85,color:"red",alpha:1},secondDecoration:{lineWidth:1,startAt:70,radius:4,fillColor:"red",color:"red",alpha:1}},chunkySwiss:{outerBorder:{lineWidth:4,radius:97,color:"black",alpha:1},smallIndicator:{lineWidth:4,startAt:89,endAt:93,color:"black",alpha:1},largeIndicator:{lineWidth:8,startAt:80,endAt:93,color:"black",alpha:1},hourHand:{lineWidth:12,startAt:-15,endAt:60,color:"black",alpha:1},minuteHand:{lineWidth:10,startAt:-15,endAt:85,color:"black",alpha:1},secondHand:{lineWidth:4,startAt:-20,endAt:85,color:"red",alpha:1},secondDecoration:{lineWidth:2,startAt:70,radius:8,fillColor:"red",color:"red",alpha:1}},chunkySwissOnBlack:{outerBorder:{lineWidth:4,radius:97,color:"white",alpha:1},smallIndicator:{lineWidth:4,startAt:89,endAt:93,color:"white",alpha:1},largeIndicator:{lineWidth:8,startAt:80,endAt:93,color:"white",alpha:1},hourHand:{lineWidth:12,startAt:-15,endAt:60,color:"white",alpha:1},minuteHand:{lineWidth:10,startAt:-15,endAt:85,color:"white",alpha:1},secondHand:{lineWidth:4,startAt:-20,endAt:85,color:"red",alpha:1},secondDecoration:{lineWidth:2,startAt:70,radius:8,fillColor:"red",color:"red",alpha:1}}},isIE:!!document.all,clockTracker:{},noIdCount:0};CoolClock.prototype={init:function(options){this.canvasId=options.canvasId;this.skinId=options.skinId||CoolClock.config.defaultSkin;this.displayRadius=options.displayRadius||CoolClock.config.defaultRadius;this.showSecondHand=typeof options.showSecondHand=="boolean"?options.showSecondHand:true;this.gmtOffset=(options.gmtOffset!=null&&options.gmtOffset!="")?parseFloat(options.gmtOffset):null;this.showDigital=typeof options.showDigital=="boolean"?options.showDigital:false;this.logClock=typeof options.logClock=="boolean"?options.logClock:false;this.logClockRev=typeof options.logClock=="boolean"?options.logClockRev:false;this.tickDelay=CoolClock.config[this.showSecondHand?"tickDelay":"longTickDelay"];this.canvas=document.getElementById(this.canvasId);this.canvas.setAttribute("width",this.displayRadius*2);this.canvas.setAttribute("height",this.displayRadius*2);this.canvas.style.width=this.displayRadius*2+"px";this.canvas.style.height=this.displayRadius*2+"px";this.renderRadius=CoolClock.config.renderRadius;this.scale=this.displayRadius/this.renderRadius;this.ctx=this.canvas.getContext("2d");this.ctx.scale(this.scale,this.scale);CoolClock.config.clockTracker[this.canvasId]=this;this.tick();return this},fullCircleAt:function(x,y,skin){this.ctx.save();this.ctx.globalAlpha=skin.alpha;this.ctx.lineWidth=skin.lineWidth;if(!CoolClock.config.isIE){this.ctx.beginPath()}if(CoolClock.config.isIE){this.ctx.lineWidth=this.ctx.lineWidth*this.scale}this.ctx.arc(x,y,skin.radius,0,2*Math.PI,false);if(CoolClock.config.isIE){this.ctx.arc(x,y,skin.radius,-0.1,0.1,false)}if(skin.fillColor){this.ctx.fillStyle=skin.fillColor;this.ctx.fill()}else{this.ctx.strokeStyle=skin.color;this.ctx.stroke()}this.ctx.restore()},drawTextAt:function(theText,x,y){this.ctx.save();this.ctx.font="15px sans-serif";var tSize=this.ctx.measureText(theText);if(!tSize.height){tSize.height=15}this.ctx.fillText(theText,x-tSize.width/2,y-tSize.height/2);this.ctx.restore()},lpad2:function(num){return(num<10?"0":"")+num},tickAngle:function(second){var tweak=3;if(this.logClock){return second==0?0:(Math.log(second*tweak)/Math.log(60*tweak))}else{if(this.logClockRev){second=(60-second)%60;return 1-(second==0?0:(Math.log(second*tweak)/Math.log(60*tweak)))}else{return second/60}}},timeText:function(hour,min,sec){var c=CoolClock.config;return""+(c.showAmPm?((hour%12)==0?12:(hour%12)):hour)+":"+this.lpad2(min)+(c.showSecs?":"+this.lpad2(sec):"")+(c.showAmPm?(hour<12?" am":" pm"):"")},radialLineAtAngle:function(angleFraction,skin){this.ctx.save();this.ctx.translate(this.renderRadius,this.renderRadius);this.ctx.rotate(Math.PI*(2*angleFraction-0.5));this.ctx.globalAlpha=skin.alpha;this.ctx.strokeStyle=skin.color;this.ctx.lineWidth=skin.lineWidth;if(CoolClock.config.isIE){this.ctx.lineWidth=this.ctx.lineWidth*this.scale}if(skin.radius){this.fullCircleAt(skin.startAt,0,skin)}else{this.ctx.beginPath();this.ctx.moveTo(skin.startAt,0);this.ctx.lineTo(skin.endAt,0);this.ctx.stroke()}this.ctx.restore()},render:function(hour,min,sec){var skin=CoolClock.config.skins[this.skinId];if(!skin){skin=CoolClock.config.skins[CoolClock.config.defaultSkin]}this.ctx.clearRect(0,0,this.renderRadius*2,this.renderRadius*2);if(skin.outerBorder){this.fullCircleAt(this.renderRadius,this.renderRadius,skin.outerBorder)}for(var i=0;i<60;i++){(i%5)&&skin.smallIndicator&&this.radialLineAtAngle(this.tickAngle(i),skin.smallIndicator);!(i%5)&&skin.largeIndicator&&this.radialLineAtAngle(this.tickAngle(i),skin.largeIndicator)}if(this.showDigital){this.drawTextAt(this.timeText(hour,min,sec),this.renderRadius,this.renderRadius+this.renderRadius/2)}if(skin.hourHand){this.radialLineAtAngle(this.tickAngle(((hour%12)*5+min/12)),skin.hourHand)}if(skin.minuteHand){this.radialLineAtAngle(this.tickAngle((min+sec/60)),skin.minuteHand)}if(this.showSecondHand&&skin.secondHand){this.radialLineAtAngle(this.tickAngle(sec),skin.secondHand)}if(!CoolClock.config.isIE&&this.showSecondHand&&skin.secondDecoration){this.radialLineAtAngle(this.tickAngle(sec),skin.secondDecoration)}},refreshDisplay:function(){var now=new Date();if(this.gmtOffset!=null){var offsetNow=new Date(now.valueOf()+(this.gmtOffset*1000*60*60));this.render(offsetNow.getUTCHours(),offsetNow.getUTCMinutes(),offsetNow.getUTCSeconds())}else{this.render(now.getHours(),now.getMinutes(),now.getSeconds())}},nextTick:function(){setTimeout("CoolClock.config.clockTracker['"+this.canvasId+"'].tick()",this.tickDelay)},stillHere:function(){return document.getElementById(this.canvasId)!=null},tick:function(){if(this.stillHere()){this.refreshDisplay();this.nextTick()}}};CoolClock.findAndCreateClocks=function(){var canvases=document.getElementsByTagName("canvas");for(var i=0;i<canvases.length;i++){var fields=canvases[i].className.split(" ")[0].split(":");if(fields[0]=="CoolClock"){if(!canvases[i].id){canvases[i].id="_coolclock_auto_id_"+CoolClock.config.noIdCount++}new CoolClock({canvasId:canvases[i].id,skinId:fields[1],displayRadius:fields[2],showSecondHand:fields[3]!="noSeconds",gmtOffset:fields[4],showDigital:fields[5]=="showDigital",logClock:fields[6]=="logClock",logClockRev:fields[6]=="logClockRev"})}}};if(window.jQuery){jQuery(document).ready(CoolClock.findAndCreateClocks)};
/*Freewall*/
!function(t){function i(i){function n(i){var e=(u.gutterX,u.gutterY,u.cellH),n=u.cellW,o=t(i),l=o.find(o.attr("data-handle"));a.setDraggable(i,{handle:l[0],onStart:function(t){s.animate&&a.transition&&a.setTransition(this,""),o.css("z-index",9999).addClass("fw-float"),s.onBlockDrag.call(i,t)},onDrag:function(t){var a=o.position(),l=Math.round(a.top/e),r=Math.round(a.left/n),c=Math.round(o.width()/n),d=Math.round(o.height()/e);l=Math.min(Math.max(0,l),u.limitRow-d),r=Math.min(Math.max(0,r),u.limitCol-c),h.setHoles({top:l,left:r,width:c,height:d}),h.refresh(),s.onBlockMove.call(i,t)},onDrop:function(a){var l=o.position(),r=Math.round(l.top/e),c=Math.round(l.left/n),d=Math.round(o.width()/n),f=Math.round(o.height()/e);r=Math.min(Math.max(0,r),u.limitRow-f),c=Math.min(Math.max(0,c),u.limitCol-d),o.removeClass("fw-float"),o.css({zIndex:"auto",top:r*e,left:c*n});var g,m,w,p;for(m=0;f>m;++m)for(g=0;d>g;++g)w=m+r+"-"+(g+c),p=u.matrix[w],p&&1!=p&&t("#"+p).removeAttr("data-position");u.holes={},o.attr({"data-width":o.width(),"data-height":o.height(),"data-position":r+"-"+c}),h.refresh(),s.onBlockDrop.call(i,a)}})}var l=t(i);"static"==l.css("position")&&l.css("position","relative");var r=Number.MAX_VALUE,h=this;a.totalGrid+=1;var s=t.extend({},a.defaultConfig),u={arguments:null,blocks:{},events:{},matrix:{},holes:{},cellW:0,cellH:0,cellS:1,filter:"",lastId:0,length:0,maxWoB:0,maxHoB:0,minWoB:r,minHoB:r,running:0,gutterX:15,gutterY:15,totalCol:0,totalRow:0,limitCol:666666,limitRow:666666,sortFunc:null,keepOrder:!1};s.runtime=u,u.totalGrid=a.totalGrid;var c=document.body.style;a.transition||(null!=c.webkitTransition||null!=c.MozTransition||null!=c.msTransition||null!=c.OTransition||null!=c.transition)&&(a.transition=!0),t.extend(h,{addCustomEvent:function(t,i){var e=u.events;return t=t.toLowerCase(),!e[t]&&(e[t]=[]),i.eid=e[t].length,e[t].push(i),this},appendBlock:function(i){var e=t(i).appendTo(l),r=null,h=[];u.arguments&&(t.isFunction(u.sortFunc)&&e.sort(u.sortFunc),e.each(function(t,i){i.index=++t,r=a.loadBlock(i,s),r&&h.push(r)}),o[s.engine](h,s),a.setWallSize(u,l),u.length=e.length,e.each(function(t,i){a.showBlock(i,s),(s.draggable||i.getAttribute("data-draggable"))&&n(i)}))},appendHoles:function(t){var i,e=[].concat(t),n={};for(i=0;i<e.length;++i)n=e[i],u.holes[n.top+"-"+n.left+"-"+n.width+"-"+n.height]=n;return this},container:l,destroy:function(){var i=l.find(s.selector).removeAttr("id");i.each(function(i,e){$item=t(e);var n=1*$item.attr("data-width")||"",a=1*$item.attr("data-height")||"";$item.width(n).height(a).css({position:"static"})})},fillHoles:function(t){if(0==arguments.length)u.holes={};else{var i,e=[].concat(t),n={};for(i=0;i<e.length;++i)n=e[i],delete u.holes[n.top+"-"+n.left+"-"+n.width+"-"+n.height]}return this},filter:function(t){return u.filter=t,u.arguments&&this.refresh(),this},fireEvent:function(t,i,e){var n=u.events;if(t=t.toLowerCase(),n[t]&&n[t].length)for(var a=0;a<n[t].length;++a)n[t][a].call(this,i,e);return this},fitHeight:function(t){var t=t?t:l.height()||e.height();this.fitZone("auto",t),u.arguments=arguments},fitWidth:function(t){var t=t?t:l.width()||e.width();this.fitZone(t,"auto"),u.arguments=arguments},fitZone:function(i,r){var c=l.find(s.selector).removeAttr("id"),d=null,f=[];r=r?r:l.height()||e.height(),i=i?i:l.width()||e.width(),u.arguments=arguments,a.resetGrid(u),a.adjustUnit(i,r,s),u.filter?(c.data("active",0),c.filter(u.filter).data("active",1)):c.data("active",1),t.isFunction(u.sortFunc)&&c.sort(u.sortFunc),c.each(function(i,e){var n=t(e);e.index=++i,d=a.loadBlock(e,s),d&&n.data("active")&&f.push(d)}),h.fireEvent("onGridReady",l,s),o[s.engine](f,s),a.setWallSize(u,l),h.fireEvent("onGridArrange",l,s),u.length=c.length,c.each(function(t,i){a.showBlock(i,s),(s.draggable||i.getAttribute("data-draggable"))&&n(i)})},fixPos:function(i){return t(i.block).attr({"data-position":i.top+"-"+i.left}),this},fixSize:function(i){return null!=i.height&&t(i.block).attr({"data-height":i.height}),null!=i.width&&t(i.block).attr({"data-width":i.width}),this},prepend:function(t){return l.prepend(t),u.arguments&&this.refresh(),this},refresh:function(){var t=arguments.length?arguments:u.arguments,i=u.arguments,e=i?i.callee:this.fitWidth;return e.apply(this,Array.prototype.slice.call(t,0)),this},reset:function(i){return t.extend(s,i),this},setHoles:function(t){var i,e=[].concat(t),n={};for(u.holes={},i=0;i<e.length;++i)n=e[i],u.holes[n.top+"-"+n.left+"-"+n.width+"-"+n.height]=n;return this},sortBy:function(t){return u.sortFunc=t,u.arguments&&this.refresh(),this},unFilter:function(){return delete u.filter,this.refresh(),this}}),l.attr("data-min-width",80*Math.floor(e.width()/80));for(var d in a.plugin)a.plugin.hasOwnProperty(d)&&a.plugin[d].call(h,s,l);e.resize(function(){u.running||(u.running=1,setTimeout(function(){u.running=0,s.onResize.call(h,l)},122),l.attr("data-min-width",80*Math.floor(e.width()/80)))})}null==t.isNumeric&&(t.isNumeric=function(t){return null!=t&&t.constructor===Number}),null==t.isFunction&&(t.isFunction=function(t){return null!=t&&t instanceof Function});var e=t(window),n=t(document),a={defaultConfig:{animate:!1,cellW:100,cellH:100,delay:0,engine:"giot",fixSize:null,gutterX:15,gutterY:15,keepOrder:!1,selector:"> div",draggable:!1,cacheSize:!0,rightToLeft:!1,bottomToTop:!1,onGapFound:function(){},onComplete:function(){},onResize:function(){},onBlockDrag:function(){},onBlockMove:function(){},onBlockDrop:function(){},onBlockReady:function(){},onBlockFinish:function(){},onBlockActive:function(){},onBlockResize:function(){}},plugin:{},totalGrid:1,transition:!1,loadBlock:function(i,e){var n=e.runtime,a=n.gutterX,o=n.gutterY,l=n.cellH,r=n.cellW,h=null,s=t(i),u=s.data("active"),c=s.attr("data-position"),d=parseInt(s.attr("data-fixSize")),f=n.lastId++ +"-"+n.totalGrid;if(s.hasClass("fw-float"))return null;s.attr({id:f,"data-delay":i.index}),e.animate&&this.transition&&this.setTransition(i,""),isNaN(d)&&(d=null),null==d&&(d=e.fixSize);var g=d?"ceil":"round";null==s.attr("data-height")&&s.attr("data-height",s.height()),null==s.attr("data-width")&&s.attr("data-width",s.width());var m=1*s.attr("data-height"),w=1*s.attr("data-width");e.cacheSize||(i.style.width="",w=s.width(),i.style.height="",m=s.height());var p=w?Math[g]((w+a)/r):0,v=m?Math[g]((m+o)/l):0;if(d||"auto"!=e.cellH||(s.width(r*p-a),i.style.height="",m=s.height(),v=m?Math.round((m+o)/l):0),d||"auto"!=e.cellW||(s.height(l*v-o),i.style.width="",w=s.width(),p=w?Math.round((w+a)/r):0),null!=d&&(p>n.limitCol||v>n.limitRow))h=null;else if(v&&v<n.minHoB&&(n.minHoB=v),p&&p<n.minWoB&&(n.minWoB=p),v>n.maxHoB&&(n.maxHoB=v),p>n.maxWoB&&(n.maxWoB=p),0==w&&(p=0),0==m&&(v=0),h={resize:!1,id:f,width:p,height:v,fixSize:d},c){c=c.split("-"),h.y=1*c[0],h.x=1*c[1],h.width=null!=d?p:Math.min(p,n.limitCol-h.x),h.height=null!=d?v:Math.min(v,n.limitRow-h.y);var x=h.y+"-"+h.x+"-"+h.width+"-"+h.height;u?(n.holes[x]={id:h.id,top:h.y,left:h.x,width:h.width,height:h.height},this.setBlock(h,e)):delete n.holes[x]}return null==s.attr("data-state")?s.attr("data-state","init"):s.attr("data-state","move"),e.onBlockReady.call(i,h,e),c&&u?null:h},setBlock:function(t,i){var e=i.runtime,n=e.gutterX,a=e.gutterY,o=t.height,l=t.width,r=e.cellH,h=e.cellW,s=t.x,u=t.y;i.rightToLeft&&(s=e.limitCol-s-l),i.bottomToTop&&(u=e.limitRow-u-o);var c={fixSize:t.fixSize,resize:t.resize,top:u*r,left:s*h,width:h*l-n,height:r*o-a};return c.top=1*c.top.toFixed(2),c.left=1*c.left.toFixed(2),c.width=1*c.width.toFixed(2),c.height=1*c.height.toFixed(2),t.id&&(e.blocks[t.id]=c),c},showBlock:function(i,e){function n(){if(s&&r.attr("data-state","start"),e.animate&&h.transition&&h.setTransition(i,u),l)l.fixSize&&(l.height=1*r.attr("data-height"),l.width=1*r.attr("data-width")),r.css({opacity:1,width:l.width,height:l.height}),r[o]({top:l.top,left:l.left}),null!=r.attr("data-nested")&&h.nestedGrid(i,e);else{var t=parseInt(i.style.height)||0,n=parseInt(i.style.width)||0,c=parseInt(i.style.left)||0,d=parseInt(i.style.top)||0;r[o]({left:c+n/2,top:d+t/2,width:0,height:0,opacity:0})}a.length-=1,e.onBlockFinish.call(i,l,e),0==a.length&&e.onComplete.call(i,l,e)}var a=e.runtime,o=e.animate&&!this.transition?"animate":"css",l=a.blocks[i.id],r=t(i),h=this,s="move"!=r.attr("data-state"),u=s?"width 0.5s, height 0.5s":"top 0.5s, left 0.5s, width 0.5s, height 0.5s, opacity 0.5s";i.delay&&clearTimeout(i.delay),r.hasClass("fw-float")||(h.setTransition(i,""),i.style.position="absolute",e.onBlockActive.call(i,l,e),l&&l.resize&&e.onBlockResize.call(i,l,e),e.delay>0?i.delay=setTimeout(n,e.delay*r.attr("data-delay")):n())},nestedGrid:function(e,n){var a,o=t(e),l=n.runtime,r=o.attr("data-gutterX")||n.gutterX,h=o.attr("data-gutterY")||n.gutterY,s=o.attr("data-method")||"fitZone",u=o.attr("data-nested")||"> div",c=o.attr("data-cellH")||n.cellH,d=o.attr("data-cellW")||n.cellW,f=l.blocks[e.id];if(f)switch(a=new i(o),a.reset({cellH:c,cellW:d,gutterX:1*r,gutterY:1*h,selector:u,cacheSize:!1}),s){case"fitHeight":a[s](f.height);break;case"fitWidth":a[s](f.width);break;case"fitZone":a[s](f.width,f.height)}},adjustBlock:function(i,e){var n=e.runtime,a=n.gutterX,o=n.gutterY,l=t("#"+i.id),r=n.cellH,h=n.cellW;"auto"==e.cellH&&(l.width(i.width*h-a),l[0].style.height="",i.height=Math.round((l.height()+o)/r))},adjustUnit:function(i,e,n){var a=n.gutterX,o=n.gutterY,l=n.runtime,r=n.cellW,h=n.cellH;if(t.isFunction(r)&&(r=r(i)),r=1*r,!t.isNumeric(r)&&(r=1),t.isFunction(h)&&(h=h(e)),h=1*h,!t.isNumeric(h)&&(h=1),t.isNumeric(i)){1>r&&(r*=i);var s=Math.max(1,Math.floor(i/r));t.isNumeric(a)||(a=(i-s*r)/Math.max(1,s-1),a=Math.max(0,a)),s=Math.floor((i+a)/r),l.cellW=(i+a)/Math.max(s,1),l.cellS=l.cellW/r,l.gutterX=a,l.limitCol=s}if(t.isNumeric(e)){1>h&&(h*=e);var u=Math.max(1,Math.floor(e/h));t.isNumeric(o)||(o=(e-u*h)/Math.max(1,u-1),o=Math.max(0,o)),u=Math.floor((e+o)/h),l.cellH=(e+o)/Math.max(u,1),l.cellS=l.cellH/h,l.gutterY=o,l.limitRow=u}t.isNumeric(i)||(1>r&&(r=l.cellH),l.cellW=1!=r?r*l.cellS:1,l.gutterX=a,l.limitCol=666666),t.isNumeric(e)||(1>h&&(h=l.cellW),l.cellH=1!=h?h*l.cellS:1,l.gutterY=o,l.limitRow=666666),l.keepOrder=n.keepOrder},resetGrid:function(t){t.blocks={},t.length=0,t.cellH=0,t.cellW=0,t.lastId=1,t.matrix={},t.totalCol=0,t.totalRow=0},setDraggable:function(i,e){var a=!1,o={startX:0,startY:0,top:0,left:0,handle:null,onDrop:function(){},onDrag:function(){},onStart:function(){}};t(i).each(function(){function i(t){return t.stopPropagation(),t=t.originalEvent,t.touches&&(a=!0,t=t.changedTouches[0]),2!=t.button&&3!=t.which&&(h.onStart.call(u,t),h.startX=t.clientX,h.startY=t.clientY,h.top=parseInt(c.css("top"))||0,h.left=parseInt(c.css("left"))||0,n.bind("mouseup touchend",r),n.bind("mousemove touchmove",l)),!1}function l(t){t=t.originalEvent,a&&(t=t.changedTouches[0]),c.css({top:h.top-(h.startY-t.clientY),left:h.left-(h.startX-t.clientX)}),h.onDrag.call(u,t)}function r(t){t=t.originalEvent,a&&(t=t.changedTouches[0]),h.onDrop.call(u,t),n.unbind("mouseup touchend",r),n.unbind("mousemove touchmove",l)}var h=t.extend({},o,e),s=h.handle||this,u=this,c=t(u),d=t(s),f=c.css("position");"absolute"!=f&&c.css("position","relative"),c.find("iframe, form, input, textarea, .ignore-drag").each(function(){t(this).on("touchstart mousedown",function(t){t.stopPropagation()})}),n.unbind("mouseup touchend",r),n.unbind("mousemove touchmove",l),d.unbind("mousedown touchstart").bind("mousedown touchstart",i)})},setTransition:function(i,e){var n=i.style,a=t(i);!this.transition&&a.stop?a.stop():null!=n.webkitTransition?n.webkitTransition=e:null!=n.MozTransition?n.MozTransition=e:null!=n.msTransition?n.msTransition=e:null!=n.OTransition?n.OTransition=e:n.transition=e},getFreeArea:function(t,i,e){for(var n=Math.min(t+e.maxHoB,e.limitRow),a=Math.min(i+e.maxWoB,e.limitCol),o=a,l=n,r=e.matrix,h=t;l>h;++h)for(var s=i;a>s;++s)r[h+"-"+s]&&s>i&&o>s&&(o=s);for(var h=t;n>h;++h)for(var s=i;o>s;++s)r[h+"-"+s]&&h>t&&l>h&&(l=h);return{top:t,left:i,width:o-i,height:l-t}},setWallSize:function(t,i){var e=t.totalRow,n=t.totalCol,a=t.gutterY,o=t.gutterX,l=t.cellH,r=t.cellW,h=Math.max(0,r*n-o),s=Math.max(0,l*e-a);i.attr({"data-total-col":n,"data-total-row":e,"data-wall-width":Math.ceil(h),"data-wall-height":Math.ceil(s)}),t.limitCol<t.limitRow&&!i.attr("data-height")&&i.height(Math.ceil(s))}},o={giot:function(t,i){function e(t,i,e,n,a){for(var o=i;i+a>o;){for(var l=e;e+n>l;)g[o+"-"+l]=t,++l>s&&(s=l);++o>u&&(u=o)}}var n=i.runtime,o=n.limitRow,l=n.limitCol,r=0,h=0,s=n.totalCol,u=n.totalRow,c={},d=n.holes,f=null,g=n.matrix,m=Math.max(l,o),w=null,p=null,v=o>l?1:0,x=null,M=Math.min(l,o);for(var k in d)d.hasOwnProperty(k)&&e(d[k].id||!0,d[k].top,d[k].left,d[k].width,d[k].height);for(var B=0;m>B&&t.length;++B){v?h=B:r=B,x=null;for(var y=0;M>y&&t.length;++y)if(f=null,v?r=y:h=y,!n.matrix[h+"-"+r]){if(w=a.getFreeArea(h,r,n),null==i.fixSize){if(x&&!v&&n.minHoB>w.height){x.height+=w.height,x.resize=!0,e(x.id,x.y,x.x,x.width,x.height),a.setBlock(x,i);continue}if(x&&v&&n.minWoB>w.width){x.width+=w.width,x.resize=!0,e(x.id,x.y,x.x,x.width,x.height),a.setBlock(x,i);continue}}if(n.keepOrder)f=t.shift(),f.resize=!0;else{for(var k=0;k<t.length;++k)if(!(t[k].height>w.height||t[k].width>w.width)){f=t.splice(k,1)[0];break}if(null==f&&null==i.fixSize)for(var k=0;k<t.length;++k)if(null==t[k].fixSize){f=t.splice(k,1)[0],f.resize=!0;break}}if(null!=f)f.resize&&(v?(f.width=w.width,"auto"==i.cellH&&a.adjustBlock(f,i),f.height=Math.min(f.height,w.height)):(f.height=w.height,f.width=Math.min(f.width,w.width))),c[f.id]={id:f.id,x:r,y:h,width:f.width,height:f.height,resize:f.resize,fixSize:f.fixSize},x=c[f.id],e(x.id,x.y,x.x,x.width,x.height),a.setBlock(x,i);else{var p={x:r,y:h,fixSize:0};if(v){p.width=w.width,p.height=0;for(var z=r-1,b=h;g[b+"-"+z];)g[b+"-"+r]=!0,p.height+=1,b+=1}else{p.height=w.height,p.width=0;for(var b=h-1,z=r;g[b+"-"+z];)g[h+"-"+z]=!0,p.width+=1,z+=1}i.onGapFound(a.setBlock(p,i),i)}}}n.matrix=g,n.totalRow=u,n.totalCol=s}};i.addConfig=function(i){t.extend(a.defaultConfig,i)},i.createEngine=function(i){t.extend(o,i)},i.createPlugin=function(i){t.extend(a.plugin,i)},i.getMethod=function(t){return a[t]},window.Freewall=window.freewall=i}(window.Zepto||window.jQuery);



$(function() {
	
	$(".tile").append("<i class='spinner fa fa-refresh fa-spin'></i>");
	
	setIcons();
	
	$(".refresh, .clock").click(function() {
        refresh();
	});
    
	startTime();
	
	$(".dashboard").click(function(e) {
		animateClick($(this));
		e.stopImmediatePropagation();
		e.preventDefault();
		$(".refresh .icon").addClass("fa-spin");
		window.location = $(this).find("a").attr("href");
	});
	
	$(".history.tile").click(function(e) {
		animateClick($(this));
		e.stopImmediatePropagation();
		e.preventDefault();
		window.location = "history" + (getUrlParameter("access_token") ? "?access_token=" + getUrlParameter("access_token") : "");
	});
	
	if (readOnlyMode) {return false;}
	
	$(".switch, .dimmer, .momentary, .clock, .lock, .link, .themeLight, .camera, .music i, .light, .dimmerLight").click(function() {
		animateClick($(this));
	});
	
	$(".switch, .light, .lock, .momentary, .themeLight, .camera").click(function() {
		$(this).closest(".tile").toggleClass("active");
        sendCommand($(this).attr("data-type"), $(this).attr("data-device"), "toggle");
	});
	
	$(".dimmer, .dimmerLight").click(function() {
		$(this).toggleClass("active");
    	sendCommand($(this).attr("data-type"), $(this).attr("data-device"), "toggle", $(this).attr("data-level"));
    });
	
    $(".dimmer, .dimmerLight").on('slidestop', function(e) {
    	var level = $(this).find("input").val()
		if ($(this).hasClass("active")) {
			animateClick($(this));
			sendCommand($(this).attr("data-type"), $(this).attr("data-device"), "level", level);
		};
		$(this).attr("data-level", level);
    });
	
	 $(".music").on('slidestop', function(e) {
    	var level = $(this).find("input").val()
		animateClick($(this));
		sendCommand("music", $(this).attr("data-device"), "level", level);
		$(this).attr("data-level", level);
    });
    
	$(".music .play").click(function() {
		var tile = $(this).closest(".tile");
		$(this).closest(".tile").toggleClass("active");
		sendCommand("music", tile.attr("data-device"), "play");
	});
	
	$(".music .pause").click(function() {
		var tile = $(this).closest(".tile");
		$(this).closest(".tile").toggleClass("active");
		sendCommand("music", tile.attr("data-device"), "pause");
	});
	
	$(".music .muted").click(function() {
		var tile = $(this).closest(".tile");
		$(this).closest(".tile").toggleClass("muted");
		sendCommand("music", tile.attr("data-device"), "unmute");
	});
	
	$(".music .unmuted").click(function() {
		var tile = $(this).closest(".tile");
		$(this).closest(".tile").toggleClass("muted");
		sendCommand("music", tile.attr("data-device"), "mute");
	});
	
	$(".music .back").click(function() {
		var tile = $(this).closest(".tile");
		sendCommand("music", tile.attr("data-device"), "previousTrack");
	});
	
	$(".music .forward").click(function() {
		var tile = $(this).closest(".tile");
		sendCommand("music", tile.attr("data-device"), "nextTrack");
	});
	
	$(".mode, .hello-home").click(function() {
		$("#" + $(this).attr("data-popup")).popup("open");
    });
    
    $("#mode-popup li").click(function() {
    	$("#mode-popup").popup("close");
		var tile = $(".mode");
    	animateClick(tile);
        var newMode = $(this).text();
		sendCommand("mode", "mode", newMode);
        
		var oldMode = $(".mode").attr("data-mode");
		tile.removeClass(oldMode);
		tile.attr("data-mode", newMode);
		if (["Home", "Away", "Night"].indexOf(newMode) >= 0) {
			$("#mode-name").hide();
			tile.addClass(newMode);
		} else {
			$("#mode-name").html(newMode).show();
		}
    });
    
    $("#hello-home-popup li").on("click", function() {
    	$("#hello-home-popup").popup("close");
		animateClick($(".hello-home"));
		sendCommand("helloHome", "helloHome", $(this).text());
    });
	
	$(".thermostat:not(.null-setpoint) .up").click(function(){
		thermostatEvent($(this).closest(".tile"), 1);
	});
	
	$(".thermostat:not(.null-setpoint) .down").click(function(){
		thermostatEvent($(this).closest(".tile"), -1);
	});
	
	$(".video.smv img").each(function() {
		var image = $(this);
		setInterval(function() {
			var tmp = new Image();
			tmp.src = image.data("src") + "&rand=" + Math.random();
			image.attr("src", tmp.src);
		}, 5000);
	});
});

function thermostatEvent(tile, direction) {
	if (window[tile.data("device")]) {
		clearTimeout(window[tile.data("device")]);
	}
	var setpoint = parseInt(tile.attr("data-setpoint"));
	
	if (setpoint < maxTemp && setpoint > minTemp) {
		setpoint = setpoint + direction;
		tile.find(".icon.setpoint .whole").html(setpoint);
	}
	
	tile.attr("data-setpoint", setpoint);
	window[tile.data("device")] = setTimeout(function(){
		animateClick(tile);
		sendCommand(tile.attr("data-type"), tile.attr("data-device"), "setpoint", setpoint);
	}, 500);
}

var fadeOn = 100;
var fadeOff = 200;

function animateClick(element) {
	spinner(element);
	element.closest(".tile").animate({opacity: 0.3}, fadeOff, "swing").delay(fadeOn).animate({opacity: 1}, fadeOn, "swing");
}

function spinner(element) {
	element.closest(".tile").find(".spinner").fadeIn("slow").delay(2000).fadeOut("slow");
}

function setIcons() {
	$(".switch").append("<div class='icon'>" + icons.switch.on + icons.switch.off + "</div>");
	$(".dimmer").append("<div class='icon'>" + icons.dimmer.on + icons.dimmer.off + "</div>");
	$(".light").append("<div class='icon'>" + icons.light.on + icons.light.off + "</div>");
	$(".dimmerLight").append("<div class='icon'>" + icons.light.on + icons.light.off + "</div>");
	$(".themeLight").append("<div class='icon'>" + icons.themeLight.on + icons.themeLight.off + "</div>");
	$(".lock").append("<div class='icon'>" + icons.lock.locked + icons.lock.unlocked + "</div>");
	$(".motion").append("<div class='icon'>" + icons.motion.active + icons.motion.inactive + "</div>");
	$(".acceleration").append("<div class='icon'>" + icons.acceleration.active + icons.acceleration.inactive + "</div>");
	$(".presence").append("<div class='icon'>" + icons.presence.present + icons.presence.notPresent + "</div>");
	$(".contact").append("<div class='icon'>" + icons.contact.open + icons.contact.closed + "</div>");
	$(".water").append("<div class='icon'>" + icons.water.dry + icons.water.wet + "</div>");

	$(".dimmer, .dimmerLight, .music").each(function(){renderSlider($(this))});
	
	$(".momentary").append("<div class='icon'>" + icons.momentary + "</div>");
	$(".camera").append("<div class='icon'>" + icons.camera + "</div>");
	$(".refresh").append("<div class='icon'>" + icons.refresh + "</div>");
	$(".history").append("<div class='icon'>" + icons.history + "</div>");
	$(".hello-home").append("<div class='icon'>" + icons["hello-home"] + "</div>");
	
	$(".humidity").append("<div class='footer'>" + icons.humidity + "</div>");
	$(".luminosity").append("<div class='footer'>" + icons.luminosity + "</div>");
	$(".temperature").append("<div class='footer'>" + icons.temperature + "</div>");
	$(".energy").append("<div class='footer'>" + icons.energy + "</div>");
	$(".power").append("<div class='footer'>" + icons.power + "</div>");
	$(".battery").append("<div class='footer'>" + icons.battery + "</div>");
	
	$(".link").find("a").html(icons.link);
	$(".dashboard").find("a").html(icons.dashboard);
	
	$(".tile[data-is-value=true]").each(function(){renderValue($(this))});
}

function renderSlider(tile) {
	tile.find(".slider-container").remove();
	tile.append("<div class='slider-container'><div class='full-width-slider'><input value='" + tile.attr("data-level") + "' min='1' max='10' type='range' step='1' data-mini='true' data-popup-enabled='true' data-disabled='" + readOnlyMode + "' data-highlight='true'></div></div>").find("input").slider()
	$(".full-width-slider").click(function(e) {e.stopImmediatePropagation();});
}

function renderValue(tile) {
	tile.find(".icon").remove();
	tile.append("<div class='icon text'>" + tile.attr("data-value") + "</div>");
}

function updateWeather(tile, data) {
	tile.find(".title2").html(data.weather + ", feels like " + data.feelsLike + "&deg;")
	tile.find(".icon.text").html(data.temperature + "&deg;");
	tile.find(".icon i").attr("class", "wi " + data.icon);
	tile.find(".footer").html(data.localSunrise + ' <i class="fa fa-fw wi wi-horizon-alt"></i> ' + data.localSunset);
	tile.find(".footer.right").html(data.percentPrecip + "%<i class='fa fa-fw fa-umbrella'></i><br>" + data.humidity + "%<i class='fa fa-fw wi wi-sprinkles'></i>");
}

function updateThermostat(tile, data) {
	tile.find(".title2").html(data.temperature + "&deg;, " + data.thermostatOperatingState);
	tile.find(".icon.setpoint .whole").html(data.setpoint);
	tile.find(".footer").html("&#10044; " + data.thermostatFanMode + (data.humidity ? ",<i class='fa fa-fw wi wi-sprinkles'></i>" + data.humidity  + "%" : ""));
	tile.attr("data-setpoint", data.setpoint);
	tile.removeClass("null-setpoint");
	if (!data.setpoint) {
		tile.addClass("null-setpoint");
	}
}

function sendCommand(type, device, command, value) {
	//alert("&type=" + type + "&device=" + device + "&command=" + command + "&value=" + value);
	
	var access_token = getUrlParameter("access_token");
	var request = { type: type, device: device, command: command, value: value};
	if (access_token) request["access_token"] = access_token;
	
	$.get("command", request).done(function( data ) {
		//alert( "Data Loaded: " + data);
		if (data.status == "ok") {
			nextPoll(5);
		}
	}).fail(function() {
		setWTFCloud();
		nextPoll(10);
	});
}

function doPoll(func) {
	nextPoll(20);
	if (!func) spinner($(".refresh"));
	var access_token = getUrlParameter("access_token");
	var request = {ts:stateTS, stateID: stateID};
	if (access_token) request["access_token"] = access_token;
	
	$.get("ping", request).done(function( data ) {
		if (data.status == "reload") {
			$(".refresh .icon").addClass("fa-spin");
			location.reload();
			return;
		}
		clearWTFCloud();
		if (func) {
			func();
		} else {
			stateTS = data.ts;
			$(".refresh .footer").html("Updated " + data.updated);
			if (data.status == "update") {for (i in data.data) updateTile(data.data[i]);}
		}
	}).fail(function() {
		setWTFCloud();
	});
}

function updateTile(data) {
	if (data.tile == "device") {
		var tile = $("." + data.type + "[data-device=" + data.device + "]");
	
		if (data.type == "music") {
			if (data.trackDescription != tile.attr("data-track-description") || (data.mute + "") != tile.attr("data-mute")) spinner(tile);
			tile.attr("data-track-description", data.trackDescription);
			if ((data.mute + "") != tile.attr("data-mute")) tile.toggleClass("muted");
			tile.attr("data-mute", data.mute);
			tile.find(".title .track").html(tile.attr("data-track-description"));
		}
		
		if (data.type == "thermostatHeat" || data.type == "thermostatCool") {
			checkDataForUpdates(tile, data);
			updateThermostat(tile, data);
		} else if (data.type == "weather") {
			checkDataForUpdates(tile, data);
			updateWeather(tile, data);
		} else {
			if (data.value != tile.attr("data-value")) spinner(tile);
			tile.attr("data-value", data.value);
			
			if (data.isValue){
				renderValue(tile);
			} else {
				tile.removeClass("inactive active").addClass(data.active);
				tile.attr("data-active", data.active);
			}
			
			if (data.type == "dimmer" || data.type == "dimmerLight" || data.type == "music") {
				if (data.level != tile.attr("data-level")) spinner(tile);
				tile.attr("data-level", data.level);
				renderSlider(tile);
			}
		}
	} else if (data.tile == "mode") {
		var tile = $(".mode");
		if (data.mode != tile.attr("data-mode")) spinner(tile);
		tile.removeClass(tile.attr("data-mode"));
		tile.attr("data-mode", data.mode);
		if (data.isStandardMode) tile.addClass(data.mode);
		$(".mode-name").html(data.mode);
	}
}

function checkDataForUpdates(tile, newData) {
	newData.name = null;
	var oldData = tile.attr("data-data")
	if (oldData) {
		try {
			oldData = JSON.parse(oldData);
			for (k in oldData) {
				if(oldData[k] != "" + newData[k]) {
					spinner(tile);
					break;
				}
			}
		} catch (e) {
			spinner(tile);
		}
	} else {
		spinner(tile);
	}
	tile.attr("data-data", JSON.stringify(newData));
}

var polling;
var wtfCloud = false;

function setWTFCloud() {
	wtfCloud = true;
	$("#wtfcloud-popup").popup("open");
}

function clearWTFCloud() {
	wtfCloud = false;
	$("#wtfcloud-popup").popup("close");
}

function nextPoll(timeout) {
	if (polling) clearInterval(polling)
	polling = setInterval(function () {doPoll()}, timeout * 1000);
}

nextPoll(30);

function refresh(timeout) {
	if (!timeout) {
		setTimeout(function() { doRefresh() }, 100);
	} else {
		setTimeout(function() { doRefresh() }, timeout * 1000);
	}
}

function doRefresh() {
	$(".refresh .icon").addClass("fa-spin");
	doPoll(function func(){
		location.reload();
	});
}

refresh(60 * 60); // hard refresh every hour

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

function getClockColor() {
	if (theme == "quartz") return "#555";
	if (theme == "onyx") return "wheat";
	return "white";
}

CoolClock.config.skins = { 
st: {
	outerBorder:      { lineWidth: 12, radius: 100, color: "yellow", alpha: 0 },
		smallIndicator:   { lineWidth: 16, startAt: 80, endAt: 85, color: getClockColor(), alpha: 1 },
		largeIndicator:   { lineWidth: 2, startAt: 80, endAt: 85, color: getClockColor(), alpha: 1 },
		hourHand:         { lineWidth: 8, startAt: 0, endAt: 60, color: getClockColor(), alpha: 1 },
		minuteHand:       { lineWidth: 6, startAt: 0, endAt: 75, color: getClockColor(), alpha: 1 },
		secondHand:       { lineWidth: 5, startAt: 80, endAt: 85, color: "red", alpha: 0 },
		secondDecoration: { lineWidth: 3, startAt: 96, radius: 4, fillColor: getClockColor(), color: "black", alpha: 1 }
},
st1: {
outerBorder: { lineWidth: 2, radius: 80, color: getClockColor(), alpha: 0 },
smallIndicator: { lineWidth: 5, startAt: 88, endAt: 94, color: "yellow", alpha: 0 },
largeIndicator: { lineWidth: 5, startAt: 90, endAt: 94, color: getClockColor(), alpha: 1 },
hourHand: { lineWidth: 8, startAt: 0, endAt: 60, color: getClockColor(), alpha: 1 },
minuteHand: { lineWidth: 8, startAt: 0, endAt: 80, color: getClockColor(), alpha: 1 },
secondHand: { lineWidth: 5, startAt: 89, endAt: 94, color: getClockColor(), alpha: 1 },
secondDecoration: { lineWidth: 3, startAt: 0, radius: 4, fillColor: "black", color: "black", alpha: 0 }
}
}

function startTime() {
    if (!document.getElementById('clock')) return;
    var today=new Date();
    var h=today.getHours();
	var pm = h > 12;
    if (h > 12) {
    	h = h - 12;
    }
	
	if (h == 0) {
		h = 12;
	}
	
    var m=today.getMinutes();
    var s=today.getSeconds();
    m = checkTime(m);
    s = checkTime(s);
    document.getElementById('clock').innerHTML = h + ":" + m + (pm ? "pm" : "am");
    setTimeout(function(){startTime()},500);
}

function checkTime(i) {
    if (i<10) {i = "0" + i};  // add zero in front of numbers < 10
    return i;
}

var cellSize = getUrlParameter("t") || tileSize;
var cellGutter = getUrlParameter("g") || 4;
