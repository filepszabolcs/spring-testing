import React from 'react'
import { configure, shallow } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';

import OrcList from './OrcList';

configure({adapter: new Adapter()});

describe('<OrcList />', () => {

    let wrapper;

    beforeEach(() => {
        wrapper = shallow(
            <OrcList />
        )
    })

    it('has a table with 7 columns', () => {
        expect(wrapper.find('.table').first().find('th')).toHaveLength(7);
    })

    it('has title with "Orc list"', () => {
        expect(wrapper.find('h3').first().text()).toBe('Orc list');
    })

    it('renders correctly', () => {
        expect(wrapper).toMatchSnapshot();
    })

})