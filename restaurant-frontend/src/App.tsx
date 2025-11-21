import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Login from "./pages/Login";
import Register from "./pages/Register";
import Restaurant from "./pages/Restaurant";
import CreateRestaurant from './pages/CreateRestaurant';

function App() {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/login" element={<Login/>}/>
            <Route path="/register" element={<Register/>}/>
            <Route path="/restaurant" element={<Restaurant/>}/>
            <Route path="/create_restaurant" element={<CreateRestaurant/>}/>
        </Routes>
    </BrowserRouter>
  )
}

export default App
