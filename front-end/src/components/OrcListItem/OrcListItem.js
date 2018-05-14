import React from 'react';
import {Link} from 'react-router-dom';

const orcListItem = (props) => (
    <tr>
        <td>{props.orc.id}</td>
        <td>{props.orc.name}</td>
        <td>{props.orc.orcRaceType}</td>
        <td>{props.orc.killCount}</td>
        <td>{props.orc.weapons.join(', ')}</td>
        <td>
            <Link to={'/editOrc/' + props.orc.id}>
                <button type="button" className="btn btn-primary">Edit</button>
            </Link>
        </td>
        <td>
            <button type="button" className="btn btn-danger" onClick={() => props.deleteHandler(props.orc.id)}>Delete</button>
        </td>
    </tr>
);

export default orcListItem;