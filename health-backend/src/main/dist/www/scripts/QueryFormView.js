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
			}
		});

	},

	render: function() {

		var that = this;

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

		$('.mobileSelect').mobileSelect({
			buttonSave: '保存',
		    buttonClear: '清空',
		    buttonCancel: '取消'
		});

		var map = new BMap.Map('allmap');
		

		//判断手机浏览器是否支持定位
		if(navigator.geolocation){

			// navigator.geolocation.getCurrentPosition(function(r){},function(e){
			// 	console.log(e);
			// });
			// console.log(navigator.geolocation);

			var geolocation = new BMap.Geolocation();//创建定位实例

			geolocation.getCurrentPosition(
				function(r){

				    if(this.getStatus() == BMAP_STATUS_SUCCESS){//定位成功
				        //新建中心点 并将地图中心移动过去
				        var centerPoint = new BMap.Point(r.longitude, r.latitude);
				        that.log = r.longitude;
				        that.lat = r.latitude;

				        // console.log(that.log);
				        // console.log(that.lat);

				        map.centerAndZoom(centerPoint, 15);
				        map.panTo(centerPoint);
				        map.disableDragging();
				        //新建标注
				        var mk = new BMap.Marker(centerPoint);
				        mk.disableDragging();// 不可拖拽
				        map.addOverlay(mk);
				    }
				    else {
				        alert('failed: ' + this.getStatus());//定位失败
				    }        
				}, 
				{enableHighAccuracy: true}
			); //enableHighAccuracy 要求浏览器获取最佳结果
		} 
		else {
			var point = new BMap.Point(116.331398,39.897445);
			map.centerAndZoom(point, 15);//初始化地图，point为中心点，缩放级别为16
			map.disableDragging();
			map.addControl(new BMap.GeolocationControl());//添加定位控件 支持定位
		}
	},

	query: function() {

		window.app.navigate('query/' 
			+ this.$('#sections').val() + '/'
			+ this.$('#services').val() + '/'
			+ this.$('#preference').val() + '/'
			+ this.log + '/'
			+ this.lat, {trigger: true});
	}
});