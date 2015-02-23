var data, graph, i, point, random, scales, series, _i, _j, _k, _l, _len, _len1, _len2, _ref;
var isFirst=true;
var value=0.0;
var value2=0.0;
var mini=0.0;
var yaxis;
var _intervalsArray = [];
var logScale;
var legend;
var shelving;
var data1=[];
data = [[], []];
var now = new Date().getTime();
var day;
var legend;

var isGraphDrawed=false;
$(document).ready(function() {

	/*
	 * Filling list of supported instruments
	 */
	var $instrumentModal = $("#instrumentsModal");
	var $instrumentTable = $("#instrumentsTable");
	getAndShowInstruments($instrumentModal, $instrumentTable);

	/*
	 * Adding tooltips
	 */
	$('#sma-acro').tooltip({
		animation : true,
		placement : 'left',
		title : 'Simple Moving Average'
	});

	$('#ema-acro').tooltip({
		animation : true,
		placement : 'left',
		title : 'Exponential Moving Average'
	});

	/*
	 * Adding scroll bar to indicators table
	 */
	$("#table-container").slimScroll({
		height : '350px'
	});

});

/**
 * Function sends ajax request REST service for supported instruments and put
 * them into a list
 * 
 * @param $modal
 *            List where You want to put instruments
 * @param $table
 */
function getAndShowInstruments($modal, $table) {
	$.getJSON('http://localhost:8880/storm-processor/service/instruments',
			function(data) {
				showInstruments($modal, data);
				bindInstrumentsClickEvents($modal, $table);
			});
}
function showInstruments($modal, instruments) {
	var $groupList = $modal.find(".list-group");
	$.each(instruments, function(key) {
		var instrumentName = instruments[key];
		addInstrumentToModalList("list-group-item", instrumentName,
				instrumentName, $groupList);
	});
}
/**
 * Function adds click events into instrument nodes in html list
 * 
 * @param $modal
 *            Modal window with a HTML list
 * @param $table
 *            HTML table where You want to store your instruments
 */
function bindInstrumentsClickEvents($modal, $table) {
	var $items = $modal.find(".list-group-item");

	$items.on('click', function() {
		var instrumentName = $(this).attr("data-value");

		if (!isInstrumentOnTable(instrumentName, $table)) {
			addInstrumentToTable(instrumentName, $table);
			setIndicatorInterval(instrumentName, "SIMPLE_MOVING_AVERAGE");
			addOrRemoveActiveClassInInstrumentModal($(this), 'ADD');
		} else {
			var $notificationPlaceholder = $("#instrumentsModal").find(
					".notification-placeholder");
			sendNotification($notificationPlaceholder,
					"<span class='glyphicon glyphicon-star'></span> Instrument <strong>"
							+ instrumentName
							+ "</strong> is already in the table.", "info",
					2000);
		}
		updateInstrumentsList($("#instruments-list"), $table);
		showInfoIfTableIsEmpty($("#table-empty-info"), $table, "instrument");

	});
}
/**
 * Function sets intervals for specific instrument and indicator type
 * 
 * @param instrumentName
 *            Name of instrument
 * @param indicatorType
 *            Type of indicator
 */
function setIndicatorInterval(instrumentName, indicatorType) {
	if (_intervalsArray[instrumentName] == null) {
		_intervalsArray[instrumentName] = [];
	}

	_intervalsArray[instrumentName][indicatorType] = window.setInterval(
			function() {
				getIndicator(instrumentName, "SIMPLE_MOVING_AVERAGE");

			}, 2000);
}
/**
 * Function checks if instrument is already int the table
 * 
 * @param instrumentName
 *            Name of an instrument
 * @param $table
 *            Table where You want to scan
 * @returns {boolean} If true - instrument is in the table, false - not
 */
function isInstrumentOnTable(instrumentName, $table) {
	var answer = false;

	$table.find(".instrument").each(
			function() {

				if (instrumentName == $(this).attr("data-value")) {
					console.log("Instrument that You want to add: "
							+ instrumentName + ". We have it in table: "
							+ $(this).attr("data-value"));
					answer = true;
					return false;
				} else {
					return true;
				}

			});

	return answer;
}
/**
 * Helper function adds link element to modal list of instruments
 * 
 * @param className
 *            Class name for link
 * @param dataValue
 *            data-value attribute value
 * @param text
 *            Text for link
 * @param $parent
 *            Append link element to this parent element
 */
function addInstrumentToModalList(className, dataValue, text, $parent) {
	var $item = $('<a href="#">');
	$item.addClass(className);
	$item.attr("data-value", dataValue);
	$item.text(text);
	$parent.append($item);
}
/**
 * Function creates table row for an instrument
 * 
 * @param instrument
 *            Name of an instrument
 * @param $table
 *            Table where You want to append an instrument row
 */
function addInstrumentToTable(instrument, $table) {
	var $tr = $("<tr>");
	$tr.addClass("instrument");
	$tr.attr("data-value", instrument);

	// Name column
	var $nameColumn = createColumnForIndicatorsTable("instrument-name", null);
	var $inputName = createInputForIndicatorTable(instrument);
	$inputName.appendTo($nameColumn);

	// SMA value column
	var $valueSmaColumn = createColumnForIndicatorsTable(
			"instrument-sma-value", null);
	var $inputSmaValue = createInputForIndicatorTable("0.0");
	$inputSmaValue.appendTo($valueSmaColumn);

	// EMA value column
	var $valueEmaColumn = createColumnForIndicatorsTable(
			"instrument-ema-value", null);
	var $inputEmaValue = createInputForIndicatorTable("0.0");
	$inputEmaValue.appendTo($valueEmaColumn);

	// Adding delete button
	var $deleteButton = createDeleteButton("delete-instrument", instrument,
			deleteInstrumentButtonFun);
	var $delBtnColumn = createColumnForIndicatorsTable("", {
		"text-align" : "center"
	});
	$delBtnColumn.append($deleteButton);

	// Appending all table structure
	$tr.append($nameColumn);
	$tr.append($valueSmaColumn);
	$tr.append($valueEmaColumn);
	$tr.append($delBtnColumn);

	$table.append($tr);
}
/**
 * Helper function creates table column with specific class name and css object
 * 
 * @param className
 *            Class name
 * @param css
 *            Css object
 * @returns {*|jQuery|HTMLElement} Column node
 */
function createColumnForIndicatorsTable(className, css) {
	var $column = $('<td>');
	$column.addClass(className);

	if (css != null) {
		$column.css(css);
	}

	return $column;
}
/**
 * Helper function creates input element with specific value
 * 
 * @param value
 *            Input value
 * @returns {*|jQuery|HTMLElement} Input element
 */
function createInputForIndicatorTable(value) {
	var $input = $('<input type="text" class="form-control" readonly />');
	$input.val(value);
	return $input;
}
/**
 * Helper function creates delete button
 * 
 * @param className
 *            Button class name
 * @param dataValue
 *            data-value attribute value
 * @param onClickFunction
 *            function invoked after clicking button
 * @returns {*|jQuery|HTMLElement} HTML button element
 */
function createDeleteButton(className, dataValue, onClickFunction) {
	var $button = $('<button class="btn btn-default">Remove</button>');
	$button.addClass(className);
	$button.attr("data-value", dataValue);
	$button.on('click', onClickFunction);
	return $button;
}
/**
 * Function deletes instrument row
 */
function deleteInstrumentButtonFun() {
	var $button = $(this);
	var $instrumentsTable = $("#instrumentsTable");

	console.log("Deleting instrument row for: " + $(this).attr("data-value"));
	if ($(this).attr("data-value") == instrumentSymbol) {
		data1.forEach( function() {
			for(var i=data1.length;i>0;i--){
			data1.shift();}
			} );
		yaxis.scale=logScale;
		g = 0;
		isFirst = true;
		dayCheck = true;
	}
	$button.closest("tr.instrument").remove();

	clearIndicatorInterval($(this).attr("data-value"));

	updateInstrumentsList($("#instruments-list"), $instrumentsTable);
	showInfoIfTableIsEmpty($("#table-empty-info"), $instrumentsTable,
			"instrument");

	var $item = $('.list-group-item[data-value="' + $(this).attr('data-value')
			+ '"]');
	addOrRemoveActiveClassInInstrumentModal($item, 'DEL');

}
/**
 * Function adds/removes class 'active' to some item node
 * 
 * @param $item
 *            HTML element
 * @param action
 *            What You want to do: ADD=add class, DEL=remove class
 */
function addOrRemoveActiveClassInInstrumentModal($item, action) {
	if (action == 'ADD') {
		$item.addClass('active');
	} else if (action == 'DEL') {
		$item.removeClass('active');
	} else {
		console.log("Action attribute incorrect!");
	}
}
/**
 * Function updates list of instruments that are in the table
 * 
 * @param $instrumentList
 *            HTML list element of instruments
 * @param $checkedTable
 *            HTML table element
 */
function updateInstrumentsList($instrumentList, $checkedTable) {

	$instrumentList.find("li").remove();
	choosenInstrumentsCounter = 0;
	$.each($checkedTable.find(".instrument"), function() {
		addInstrumentNodeToList($instrumentList, $(this).attr("data-value"));
	});

}
/**
 * Function adds single instrument node into instruments list
 * 
 * @param $instrumentList
 *            List of instruments
 * @param instrumentName
 *            Name of instrument
 */
function addInstrumentNodeToList($instrumentList, instrumentName) {
	var $li = $("<li>");
	$li.attr("data-value", instrumentName);
	$li.append($("<a href='#'>" + instrumentName + "</a>"));
	$instrumentList.append($li);
	var lis = document.getElementById("instruments-list").getElementsByTagName(
			'li');
	for ( var h = 0; h < lis.length; h++) {
		lis[h].addEventListener('click', clearPlot, true);
	}
	choosenInstrumentsCounter++;
	if (choosenInstrumentsCounter == 1) {
		instrumentSymbol = instrumentName;
		highlightCurrentInstrumentInTable(instrumentName);
	}

}
function clearPlot() {
	instrumentSymbol = (this.textContent === undefined) ? this.innerText
			: this.textContent;
	
	data1.forEach( function() {
		for(var i=data1.length;i>0;i--){
		data1.shift();}
		} );
	yaxis.scale=logScale;
	g = 0;
	isFirst = true;
	dayCheck = true;
	graph.setRenderer('line');
	var instrumentName = $(this).attr("data-value");
	highlightCurrentInstrumentInTable(instrumentName);

	return false, dayCheck = true;
}

/**
 * Function highlights current active instrument
 * 
 * @param instrumentName
 *            Name of an instrument
 */
function highlightCurrentInstrumentInTable(instrumentName) {
	var $instrumentsRows = $("#instrumentsTable").find(".instrument");
	$instrumentsRows.removeClass("active");
	$instrumentsRows.each(function() {
		if ($(this).attr("data-value") == instrumentName) {
			$(this).addClass("active");
		}
	});
}
/**
 * Function clears intervals for a specific instrument
 * 
 * @param instrumentName
 *            Name of instrument
 */
function clearIndicatorInterval(instrumentName) {
	for ( var key in _intervalsArray[instrumentName]) {
		if (_intervalsArray[instrumentName].hasOwnProperty(key)) {
			window.clearInterval(_intervalsArray[instrumentName][key]);
			_intervalsArray[instrumentName][key] = null;
		}
	}
}
function getIndicator(instrumentName, indicatorType) {
	switch (indicatorType) {
	case "SIMPLE_MOVING_AVERAGE":
		$.getJSON('http://localhost:8880/storm-processor/service/simple-moving-average',
						{
							symbol : instrumentName
						},
						function(json) {						
							var d = json.published.split("-");
							var t = json.time.split(":");
							var nowD = new Date(d[0], d[1]-1, d[2], t[0], t[1],
									t[2], 0);
							now = nowD.getTime();
							value=json.value;							
							getIndicator(instrumentName,"EXPONENTIAL_MOVING_AVERAGE");
							$("#instrumentsTable").find(
									"tr[data-value='"+ json.symbol+ "'] td.instrument-sma-value input").val(json.value);							
						});
		break;
	case "EXPONENTIAL_MOVING_AVERAGE":
		$.getJSON('http://localhost:8880/storm-processor/service/exponential-moving-average',
						{
							symbol : instrumentName
						},
						function(json) {
							$("#instrumentsTable").find("tr[data-value='"+ json.symbol+ "'] td.instrument-ema-value input").val(json.value);
							value2=json.value;
							if (instrumentSymbol == json.symbol) {
								uptd();
								$('#dateOfData').val(json.published);
								curentDate = now;
							}					
						});
		break;
	} 
	}; 
	$('#render input').on('change', function() {
		var cos=$('input[name="renderer"]:checked', '#render').val();
		graph.setRenderer(cos);
	});

	$('#bbb input').on('change', function() {
		var cos=$('input[name="offset"]:checked', '#bbb').val();
	    graph.configure({offset:cos});
	});
	$('#ccc input').on('change', function() {
		var cos=$('input[name="interpolation"]:checked', '#ccc').val();
	    graph.configure({interpolation:cos});
	});
	function init(){
		logScale = d3.scale.linear().domain([mini, value+1]);
		for (i = _i = 0; _i < 1; i = ++_i) {
			data1.push({
	            data: [ { x: (now/1000)+3600, y: value }],
	            color: "#c05020",
	            name: "SMA",
	            scale:logScale
	        });
			data1.push({
	            data: [ { x: (now/1000)+3600, y: value2 }],
	            color: "#000000",
	            name: "EMA",
	            scale:logScale
	        });
		}
	
	};
	var legend1 = document.querySelector('#legend1');

	var Hover = Rickshaw.Class.create(Rickshaw.Graph.HoverDetail, {

		render: function(args) {
			legend1.innerHTML = args.formattedXValue;

			args.detail.sort(function(a, b) { return a.order - b.order }).forEach( function(d) {

				var line = document.createElement('div');
				line.className = 'line';
				var swatch = document.createElement('div');
				swatch.className = 'swatch';
				swatch.style.backgroundColor = d.series.color;

				var label = document.createElement('div');
				label.className = 'label';
				label.innerHTML = d.name + ": " + d.formattedYValue;
				line.appendChild(swatch);
				line.appendChild(label);
				legend1.appendChild(line);
				
				var dot = document.createElement('div');
				dot.className = 'dot';
				dot.style.top = graph.y(d.value.y0 + d.value.y) + 'px';
				dot.style.borderColor = d.series.color;
				this.element.appendChild(dot);

				dot.className = 'dot active';
				if(d.name=='SMA'){
					$('#choosenSMA').val(d.formattedYValue);
				}
				else{
					$('#choosenEMA').val(d.formattedYValue);
				}
				$('#choosenData').val(d.formattedXValue);
				this.show();

			}, this );
	        }
	});
var element=document.getElementById('legend');
	function initGraph(){
		graph = new Rickshaw.Graph({
			  element: document.getElementById("placeholder"),
			  renderer: 'line', 
				stroke: true,
				preserve: true,
				offset:'zero',
			  series: data1
			});
			  yaxis=new Rickshaw.Graph.Axis.Y.Scaled({
			  element: document.getElementById('axis0'),
			  graph: graph,
			  orientation: 'left',
			  scale: logScale,
			  tickFormat: Rickshaw.Fixtures.Number.formatKMBT,
			  tickSize: 2
			});
		
			new Rickshaw.Graph.Axis.Time({
			  graph: graph
			});
			var hover = new Hover( { graph: graph } ); 
			//new Rickshaw.Graph.HoverDetail({
			//  graph: graph
			//});
			legend = new Rickshaw.Graph.Legend( {
				graph: graph,
				element: element

			} );

			 shelving = new Rickshaw.Graph.Behavior.Series.Toggle( {
				graph: graph,
				legend: legend
			} );
			graph.render();
	}
	function uptd(){
		$('#instrument').val(instrumentSymbol);
		if(isFirst==true){
			if (value > value2){
				mini=value2-0.05;
			}
			else{
			mini=value-0.05;
			}	
		init();
		if(isGraphDrawed==false){
			isGraphDrawed=true;
		initGraph();}
		yaxis.scale=logScale;
		day=new Date(now).getDay()-1;
		isFirst=false;
		 }
		else {
			if(day!=new Date(now).getDay()-1){
				if (value > value2){
					mini=value2-0.05;
				}
				else{
				mini=value-0.05;
				}
				logScale = d3.scale.linear().domain([mini, value+1]);
			
				data1.forEach( function() {
					for(var i=data1.length;i>0;i--){
					data1.shift();}
					} );
				setTimeout(init(),1000);
				yaxis.scale=logScale;
				var fieldNameElement = document.getElementById('legend');
				  while(fieldNameElement.childNodes.length >= 1) {
				    fieldNameElement.removeChild(fieldNameElement.firstChild);
				  }
				  fieldNameElement.appendChild(fieldNameElement.ownerDocument.createElement('legend'));
				//var hover = new Hover( { graph: graph } ); 
				//new Rickshaw.Graph.HoverDetail({
				//  graph: graph
				//});
			
				  legend = new Rickshaw.Graph.Legend( {
						graph: graph,
						element: element

					} );

					 shelving = new Rickshaw.Graph.Behavior.Series.Toggle( {
						graph: graph,
						legend: legend
					} );
				day=new Date(now).getDay()-1;
				graph.render();
			}
			else{
				if(value<mini){
					mini=value-0.05;
					logScale = d3.scale.linear().domain([mini, value+1]);
					data1[0].scale=logScale;
					data1[1].scale=logScale;
					yaxis.scale=logScale;
				}
				else if(value<mini){
					mini=value2-0.05;
					logScale = d3.scale.linear().domain([mini, value+1]);
					data1[0].scale=logScale;
					data1[1].scale=logScale;
					yaxis.scale=logScale;
				}
			data1[0].data.push({ x: (now/1000)+3600, y: value });
			data1[1].data.push({ x: (now/1000)+3600, y: value2 });
				
				graph.render();
				}
		}
	}