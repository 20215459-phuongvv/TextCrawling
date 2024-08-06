import { lazy, Suspense, useRef, useState } from 'react'
// styles
import './styles/index.scss';
import 'react-toastify/dist/ReactToastify.min.css';
import ThemeStyles from './styles/theme';
import { ThemeProvider } from 'styled-components';
import { ToastContainer } from 'react-toastify';

// hooks
import { useTheme } from './contexts/themeContext';
import { SidebarProvider } from './contexts/sidebarContext';
import { useWindowSize } from 'react-use';

// fonts
import './fonts/icomoon/icomoon.woff';
import { Route, Routes, useLocation } from 'react-router-dom';
import AppBar from './layouts/AppBar';
import Sidebar from './layouts/Sidebar';
import Loader from './components/Loader';
import axios from 'axios';


const Dashboard = lazy(() => import('./pages/Dashboard'))

function App() {
  const { width } = useWindowSize();
  const appRef = useRef(null);
  const { theme } = useTheme();
  const path = useLocation().pathname;
  const withSidebar = path !== '/login' && path !== '/404';


  axios.defaults.baseURL = import.meta.env.VITE_URL;

  return (
    <SidebarProvider>
      <ToastContainer />
      <ThemeProvider theme={{ theme: theme }}>
        <ThemeStyles />
        {width < 1280 && withSidebar && <AppBar />}
        <div className={`app ${!withSidebar ? 'fluid' : ''}`}>
          {withSidebar && <Sidebar/>}
          <div className="app_content">
            {width >= 1280 && withSidebar && <AppBar/>}
            <Suspense fallback={<Loader />}>
              <div className="main">
                <Routes>
                  <Route path="" element={<Dashboard />} />
                </Routes>
              </div>
            </Suspense>
          </div>
        </div>
      </ThemeProvider>
    </SidebarProvider>
  )
}

export default App
