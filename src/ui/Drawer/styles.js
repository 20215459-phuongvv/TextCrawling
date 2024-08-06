import styled from 'styled-components';
import { Drawer } from 'antd';


const StyledDrawer = styled(Drawer)`
  box-shadow: var(--shadow) !important;
  /* cursor: pointer; */
  background: var(--widget) !important;
  color: var(--text) !important;
  height: var(--app-height) !important;
  font-family: var(--heading-font);
  * {
    margin-bottom: 16px;
  }
`;

export default StyledDrawer