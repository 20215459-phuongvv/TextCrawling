import { useState, useEffect, useContext, useCallback, useRef } from 'react';
import axios from 'axios';

import { toast } from 'react-toastify';


const useAPI = (url, method) => {

  // const noti = useRef(toast)
  const [state, setState] = useState({ data: null, loading: false })

  const fetch = useCallback(async () => {
    try {

      if (!url){

        setState({ data: null, loading: false });
        return false;

      }

      setState({ loading: true });
      const res = await axios({

        url: url,
        method: method || 'get',
        
      })

      setState({ data: res.data, loading: false });

    }
    catch (err){

    //   context?.current &&
    //   context.current.handleError(err);
    console.log(err);
    
    toast.error(err?.message)

    }
  }, [url, method]);

  useEffect(() => {

    fetch()

  }, [fetch]);

  return state

}

export default useAPI