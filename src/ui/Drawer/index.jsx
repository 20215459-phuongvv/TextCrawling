import React from 'react'
import { Drawer } from 'antd'
import StyledDrawer from './styles'
import dayjs from 'dayjs'
const DrawerStyled = ({data, onClose, open}) => {
  const { title, author, time, summary, content, url } = data
  return (
    <>
      <StyledDrawer width={640} placement="right" closable={false} onClose={onClose} open={open}>
        <p className='font-bold text-xl'>{title}</p>
        <div className='flex items-center gap-5'>
          <p className='!mb-0'>{author}</p>
          <time className='!mb-0' datetime={time}>{time}</time>
          <a href={url} target="_blank" className='btn btn--outline blue !mb-0'>Go to this news</a>
        </div>
        <p className='font-bold'>
          {summary}
        </p>
        <p className='text-justify'>{content}</p>
      </StyledDrawer>
    </>
  )
}

export default DrawerStyled