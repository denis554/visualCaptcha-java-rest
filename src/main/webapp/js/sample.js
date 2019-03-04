( function( angular ) {
    angular
        .module( 'app', [ 'visualCaptcha' ] )
        .controller( 'sampleController', function( $scope,$log,SampleService ) {

            // see https://github.com/emotionLoop/visualCaptcha-frontend-angular
            $scope.captchaOptions = {
                imgPath: 'img/',
                captcha: {
                    numberOfImages: 5
                },
                init: function ( captcha ) {
                    $scope.captcha = captcha;
                }
            };

            $scope.requestId = null;
            $scope.name = "myself";
            $scope.summary = null;

            $scope.submit = function() {
                $log.info( "Submitting request" );
                var captchaData = $scope.captcha.getCaptchaData();
                var request = {
                    'name': $scope.name,
                    'summary': $scope.summary,
                    'captchaData':  captchaData
                };
                SampleService.submitRequest(request).then(function (data) {
                    $scope.requestId = data.data.requestId;
                    $log.debug("OK got requestId " + $scope.requestId );

                }, function (errorData) {
                    $log.debug("Error " + angular.toJson(errorData));
                    $scope.requestId = errorData.data.message;
                });
                $scope.summary = 'klaus';
            }
        } )

        .factory('SampleService', function ($http, $log) {
            return {
                submitRequest: function (request) {
                    var api = '/api/sample';
                    var promise = $http.post(api, request);
                    $log.debug('Calling ' + api);
                    return promise;
                } //,
            };
        });

}( angular ) );

/*
 if ( queryString.indexOf('status=noCaptcha') !== -1 ) {
 $scope.valid = false;
 $scope.status = 'visualCaptcha was not started!';
 } else if ( queryString.indexOf('status=validImage') !== -1 ) {
 $scope.valid = true;
 $scope.status = 'Image was valid!';
 } else if ( queryString.indexOf('status=failedImage') !== -1 ) {
 $scope.valid = false;
 $scope.status = 'Image was NOT valid!';
 } else if ( queryString.indexOf('status=validAudio') !== -1 ) {
 $scope.valid = true;
 $scope.status = 'Accessibility answer was valid!';
 } else if ( queryString.indexOf('status=failedAudio') !== -1 ) {
 $scope.valid = false;
 $scope.status = 'Accessibility answer was NOT valid!';
 } else if ( queryString.indexOf('status=failedPost') !== -1 ) {
 $scope.valid = false;
 $scope.status = 'No visualCaptcha answer was given!';
 }
 */
/*
 $scope.isVisualCaptchaFilled = function() {
 if ( $scope.captcha.getCaptchaData().valid ) {
 window.alert( 'visualCaptcha is filled!' );
 } else {
 window.alert( 'visualCaptcha is NOT filled!' );
 }
 };
 */
