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
        css "/lib/bootstrap/css/bootstrap.min.css"
        js "/lib/jquery/jquery.min.js"
        js "/lib/angularjs/angular.min.js"
        js "/lib/angularjs/angular-route.min.js"
        js "/lib/angularjs/angular-resource.min.js"
        js "/lib/angular-ui-bootstrap/ui-bootstrap-tpls.min.js"
        css "/lib/ui-grid/ui-grid.min.css"
        js "/lib/ui-grid/ui-grid.min.js"
        css "/lib/font-awesome/css/font-awesome.min.css"
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