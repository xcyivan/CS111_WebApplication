var xmlHttp = new XMLHttpRequest();

function SuggestionProvider(){}

SuggestionProvider.prototype.requestSuggestions = function (oAutoSuggestControl, bTypeAhead) {

    var aSuggestions = new Array();
    var qtxt = oAutoSuggestControl.textbox.value;
    var request = "/eBay/suggest?q=" + encodeURI(qtxt);
    xmlHttp.open("GET", request);
    xmlHttp.onreadystatechange = function(){
        if(xmlHttp.readyState == 4){
            var rsp = xmlHttp.responseXML;
            if(rsp!=null){
                var nodes = xmlHttp.responseXML.getElementsByTagName('CompleteSuggestion');
                for(var i=0; i<nodes.length; i++){
                    var suggestion = nodes[i].childNodes[0].getAttribute("data");
                    aSuggestions.push(suggestion);
                }
            }
        }
        oAutoSuggestControl.autosuggest(aSuggestions, false);    
    }
    
    xmlHttp.send(null);
};