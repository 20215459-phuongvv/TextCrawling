import styled from 'styled-components'
import { Select } from 'antd'
const Styled = styled(Select)`
    height: var(--element-height) !important;
    border: var(--input-border) !important;
    color: var(--text) !important;
    box-shadow: var(--shadow) !important;
    font-family: var(--heading-font);
    .ant-select-selector {
        background: transparent !important;
    }
    .ant-select-selection-item-remove {
        color: var(--text) !important;
    }
`

const StyledSelect = ({
    mode = 'multiple',
    placeholder='Select multiple tags',
    options = [],
    wrapperClass = '',
    query, setQuery,
}) => {
     
    return <Styled 
                showSearch={true} 
                defaultValue={query} 
                onChange={(value) => setQuery(value)} 
                mode={mode} 
                placeholder={placeholder} 
                options={options}
                className={wrapperClass}
            />
}

export default StyledSelect