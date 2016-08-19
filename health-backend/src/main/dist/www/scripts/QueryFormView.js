var QueryFormView = Backbone.View.extend({

	tagName: 'div',
	className: 'row',
	events: {
		'click .query' : 'query'
	},

	initialize: function() {

		var that = this;

		$.ajax({
			url: '/templates/QueryFormView.html', 
			async: false,
			success: function(data) {

				that.$el.html(data);
				that.template = _.template(data);
			}
		});

	},

	render: function() {

		$.ajax({
			url: '/sections',
			type: 'GET',
			dataType: "json",
			async: false,
			success: function(res) {

				_.each(res.data, function(v,k){
					$('#sections').append('<option value="' + k + '" text="'+ v +'">' + v + '</option>');
				});
			}
		});

		$.ajax({
			url: '/services',
			type: 'GET',
			dataType: "json",
			async: false,
			success: function(res) {

				_.each(res.data, function(v){
					$('#services').append('<option value="' + v + '" text="'+ v +'">' + v + '</option>');
				});
			}
		});

		$('.mobileSelect').mobileSelect();
		var that = this;

		var map = new BMap.Map('allmap');

		var point = new BMap.Point(116.331398,39.897445);
		map.centerAndZoom(point,16);//初始化地图，point为中心点，缩放级别为16
		//判断手机浏览器是否支持定位
		if(navigator.geolocation){
			var geolocation = new BMap.Geolocation();//创建定位实例
			geolocation.getCurrentPosition(showLocation,{enableHighAccuracy: true});//enableHighAccuracy 要求浏览器获取最佳结果
		}else{
			map.addControl(new BMap.GeolocationControl());//添加定位控件 支持定位
		}
	},

	query: function() {


	}
});