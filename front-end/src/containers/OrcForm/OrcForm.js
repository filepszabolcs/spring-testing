import React, {Component} from 'react';
import axios from 'axios';


class OrcForm extends Component {
    constructor(props) {
        super(props);
        document.title = 'Orc form';
    }

    state = {
        races: [],
        weapons: [],
        orcForm: {
            name: {
                value: '',
                isValid: true,
                message: ''
            },
            killCount: {
                value: 0,
                isValid: true,
                message: ''
            },
            raceType: {
                value: '',
                isValid: true,
                message: ''
            },
            weapons: {
                value: [],
                isValid: true,
                message: ''
            }
        }
    };

    componentDidMount() {
        axios.get('/api/orcs/formData')
            .then((response) => {
                this.setState({
                    weapons: response.data.weapons,
                    races: response.data.raceTypes,
                })
            });

        if (this.props.match.params.id) {
            axios.get('/api/orcs/' + this.props.match.params.id)
                .then((response) => {
                    const updatedOrcForm = {
                        ...this.state.orcForm
                    };

                    for (let field in response.data) {
                        const updatedFormElement = {
                            ...updatedOrcForm[field]
                        };
                        updatedFormElement.value = response.data[field];
                        updatedOrcForm[field] = updatedFormElement;
                    }

                    this.setState({orcForm: updatedOrcForm});
                })
        }
    };

    inputChangedHandler = (event) => {
        const target = event.target;
        const updatedOrcForm = {
            ...this.state.orcForm
        };
        const updatedFormElement = {
            ...updatedOrcForm[target.name]
        };

        let value;
        if (target.type === 'checkbox') {
            value = target.checked
                ? updatedFormElement.value.concat(target.value)
                : updatedFormElement.value.filter(val => val !== target.value);
        } else {
            value = target.value;
        }

        updatedFormElement.value = value;
        updatedOrcForm[target.name] = updatedFormElement;

        this.setState({orcForm: updatedOrcForm});
    };

    validationHandler = (error) => {
        const updatedOrcForm = {
            ...this.state.orcForm
        };

        for (let field in this.state.orcForm) {
            const updatedFormElement = {
                ...updatedOrcForm[field]
            };
            updatedFormElement.isValid = true;
            updatedFormElement.message = '';
            updatedOrcForm[field] = updatedFormElement;
        }

        for (let fieldError of error.fieldErrors) {
            const updatedFormElement = {
                ...updatedOrcForm[fieldError.field]
            };
            updatedFormElement.isValid = false;
            updatedFormElement.message = fieldError.message;
            updatedOrcForm[fieldError.field] = updatedFormElement;
        }

        this.setState({orcForm: updatedOrcForm});
    }

    postDataHandler = (event) => {
        event.preventDefault();

        const formData = {};
        for (let formElementIdentifier in this.state.orcForm) {
            formData[formElementIdentifier] = this.state.orcForm[formElementIdentifier].value;
        }

        let url = '/api/orcs';
        let method = 'post';
        const id = this.props.match.params.id;
        if (id) {
            url += '/' + id;
            method = 'put';
        }

        axios({method: method, url: url, data: formData})
            .then(response => {
                console.log(response);
                this.props.history.push('/orcList')
            })
            .catch(error => {
                console.log(error.response);
                this.validationHandler(error.response.data);
            });
    }

    render() {
        return (
            <div>
                <h3>Orc form</h3>
                <form onSubmit={this.postDataHandler}>
                    <div className={!this.state.orcForm.name.isValid ? "has-error" : null}>
                        <label className="control-label">Orc name:</label>
                        <input type="text"
                               name="name"
                               value={this.state.orcForm.name.value}
                               onChange={this.inputChangedHandler}
                               placeholder="Orc name"
                               className="form-control"/>
                        <span className="help-block">{this.state.orcForm.name.message}</span>
                    </div>

                    <div className={!this.state.orcForm.killCount.isValid ? "has-error" : null}>
                        <label className="control-label">Kill count:</label>
                        <input type="text"
                               name="killCount"
                               value={this.state.orcForm.killCount.value}
                               onChange={this.inputChangedHandler}
                               placeholder="Kill count"
                               className="form-control"/>
                        <span className="help-block">{this.state.orcForm.killCount.message}</span>
                    </div>

                    <div className={!this.state.orcForm.raceType.isValid ? "has-error" : null}>
                        <label className="control-label">Race:</label>
                        <select name="raceType"
                                value={this.state.orcForm.raceType.value}
                                onChange={this.inputChangedHandler}
                                className="form-control">
                            <option key="" value=""></option>
                            {Object.entries(this.state.races).map(([key, value]) => {
                                return <option key={key} value={key}>{value}</option>
                            })}
                        </select>
                        <span className="help-block">{this.state.orcForm.raceType.message}</span>
                    </div>

                    <div className={!this.state.orcForm.weapons.isValid ? "has-error" : null}>
                        <label className="control-label">Weapons:</label>
                        {Object.entries(this.state.weapons).map(([key, value]) => {
                            return (
                                <div className="checkbox" key={key}>
                                    <label title="">
                                        <input type="checkbox"
                                               name="weapons"
                                               value={key}
                                               onChange={this.inputChangedHandler}
                                               checked={this.state.orcForm.weapons.value.includes(key)}/>{value}
                                    </label>
                                </div>
                            );
                        })}
                        <span className="help-block">{this.state.orcForm.weapons.message}</span>
                    </div>

                    <button type="submit" className="btn btn-default">Submit</button>
                </form>
            </div>
        );
    }
}

export default OrcForm;



