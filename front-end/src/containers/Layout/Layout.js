import React from 'react';
import {Route, Link, Switch, Redirect} from 'react-router-dom';

import OrcForm from '../OrcForm/OrcForm'
import OrcList from '../OrcList/OrcList'

const layout = () => {
    return (
        <div>
            <nav className="navbar navbar-dark bg-dark navbar-expand-lg fixed-top">
                <a className="navbar-brand" href="/">Mordor Spring</a>
                <button type="button" className="navbar-toggler collapsed" data-toggle="collapse" data-target="#navbar"
                        aria-expanded="false" aria-controls="navbar" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"/>
                </button>
                <div id="navbar" className="navbar-collapse collapse">
                    <ul className="navbar-nav mr-auto mt-2 mt-lg-0">
                        <li className="nav-item"><Link className="nav-link" to="/orcForm">New Orc</Link></li>
                        <li className="nav-item"><Link className="nav-link" to="/">Orc list</Link></li>
                        <li className="nav-item"><Link className="nav-link" to="/hordeForm">New Horde</Link></li>
                        <li className="nav-item"><Link className="nav-link" to="/hordeList">Horde List</Link></li>
                    </ul>
                </div>
            </nav>
            <div className="container" role="main">
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
                        <Redirect from="/" to="/orcList"/>

                        <Route render={() => <h1>Not found</h1>}/>
                    </Switch>
                </div>
            </div>
        </div>
    )
}


export default layout;