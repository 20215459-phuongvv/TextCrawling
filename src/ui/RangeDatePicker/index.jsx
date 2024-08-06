import StyledRangePicker from './styles';

// hooks
import {useState, useEffect} from 'react';

// utils
import dayjs from 'dayjs';

const RangeDatePicker = ({id, value, onChange, innerRef, disableFuture = true}) => {
    const [open, setOpen] = useState(false);

    useEffect(() => {
        window.addEventListener('resize', () => setOpen(false));

        return () => window.removeEventListener('resize', () => setOpen(false));
    }, []);
    // let defaultValue = value ? value : [dayjs().startOf('year'), dayjs()]
    return (
        <StyledRangePicker className="field-input"
                           id={id}
                           allowClear={false}
                           suffixIcon={<i className="icon icon-calendar-days-regular"/>}
                           separator="-"
                           format="DD/MM/YYYY"
                           disabledDate={disableFuture ? current => current && current > dayjs().endOf('day') : null}
                        //    defaultValue={defaultValue}
                           value={value}
                           onChange={onChange}
                           onOpenChange={setOpen}
                           ref={innerRef}
                           open={open}
                           renderExtraFooter={() =>
                               <button className="btn btn--secondary w-full md:w-[252px] md:ml-auto"
                                       onClick={() => setOpen(false)}>
                                   Close
                               </button>
                           }
        />
    )
}

export default RangeDatePicker