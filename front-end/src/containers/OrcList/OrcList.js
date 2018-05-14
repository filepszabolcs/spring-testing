import React, {Component} from 'react';
import axios from "axios/index";

import OrcListItem from '../../components/OrcListItem/OrcListItem';


class OrcList extends Component {
    state = {
        orcs: []
    };

    getOrcs = () => {
        axios.get('/api/orcs')
            .then(response => {
                this.setState({
                    orcs: response.data
                })
            })
            .catch(error => {
                console.log(error.response);
            });
    }

    componentDidMount() {
        this.getOrcs();
    }

    deleteOrcHandler = (id) => {
        axios.delete('/api/orcs/' + id)
            .then(response => {
                console.log(response);
                this.getOrcs();
            })
            .catch(error => {
                console.log(error.response);
            });
    }

    render() {
        let orcs;
        orcs = this.state.orcs.map(orc => {
            return (
                <OrcListItem
                    key={orc.id}
                    orc={orc}
                    deleteHandler={this.deleteOrcHandler}
                />
            );
        });


        return (
            <div>
                <h3>Orc list</h3>
                <table className="table table-striped table-bordered table-condensed table-hover">
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>Name</th>
                        <th>Race type</th>
                        <th>Kill count</th>
                        <th>Weapons</th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                        {orcs}
                    </tbody>
                </table>
            </div>
        );
    }

};

export default OrcList;