/*
 * Copyright 2016 John Scattergood
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

groups {
    bootstrap {
        css "webjar:bootstrap/3.3.6/css/bootstrap.min.css"
        js "webjar:jquery/2.2.2/jquery.min.js"
        js "webjar:angularjs/1.5.0/angular.min.js"
        js "webjar:angularjs/1.5.0/angular-route.min.js"
        js "webjar:angularjs/1.5.0/angular-resource.min.js"
        js "webjar:angular-ui-bootstrap/1.1.1-1/ui-bootstrap-tpls.min.js"
        css "webjar:ui-grid/3.0.7/ui-grid.min.css"
        js "webjar:ui-grid/3.0.7/ui-grid.min.js"
        css "webjar:font-awesome/css/font-awesome.min.css"
    }
    app {
        groupRef "bootstrap"
        css "/less/app.less"
        js "/js/app.js"
        js "/js/controllers.js"
        js "/js/directives.js"
        js "/js/filters.js"
        js "/js/services.js"
    }
}