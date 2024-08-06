// components
import RangeDatePicker from '../ui/RangeDatePicker';

const CalendarSelector = ({wrapperClass, label = 'Sales period', id, value, onChange}) => {
    return (
        <div className={`${wrapperClass || ''} flex flex-col gap-2.5 w-full`}>
            {/* <label className="h5 w-fit" htmlFor={id}>{label}:</label> */}
            <RangeDatePicker value={value} onChange={onChange} id={id}/>
        </div>
    )
}

export default CalendarSelector