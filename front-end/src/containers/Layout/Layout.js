import React, {Component} from 'react';
import {Route, Link, Switch, Redirect} from 'react-router-dom';

import OrcForm from '../OrcForm/OrcForm'
import OrcList from '../OrcList/OrcList'

class Layout extends Component {
    render() {
        return (
            <div>
                <nav className="navbar navbar-inverse navbar-fixed-top">
                    <div className="container">
                        <div className="navbar-header">
                            <button type="button" className="navbar-toggle collapsed" data-toggle="collapse"
                                    data-target="#navbar"
                                    aria-expanded="false" aria-controls="navbar">
                                <span className="sr-only">Toggle navigation</span>
                                <span className="icon-bar"></span>
                                <span className="icon-bar"></span>
                                <span className="icon-bar"></span>
                            </button>
                            <a className="navbar-brand" href="/">Mordor Spring</a>
                        </div>
                        <div id="navbar" className="navbar-collapse collapse">
                            <ul className="nav navbar-nav">
                                <li><Link to="/orcForm">New Orc</Link></li>
                                <li><Link to="/">Orc list</Link></li>
                                <li><Link to="/hordeForm">New Horde</Link></li>
                                <li><Link to="/hordeList">Horde List</Link></li>
                            </ul>
                        </div>
                    </div>
                </nav>
                <div className="container theme-showcase" role="main">
                    <div className="jumbotron">
                        <Switch>
                            <Route path="/orcList" exact component={OrcList}/>
                            <Route path="/orcForm" exact component={OrcForm}/>
                            <Route path="/editOrc/:id" exact component={OrcForm}/>
                            {/*
                            <Route path="/hordeList" exact component={HordeList}/>
                            <Route path="/hordeForm" exact component={HordeForm}/>
                            <Route path="/editHorde/:id" exact component={HordeForm}/>
                            */}
                            <Redirect from="/" to="/orcList" />

                            <Route render={() => <h1>Not found</h1>}/>
                        </Switch>
                    </div>
                </div>
            </div>
        )
    }
}

export default Layout;