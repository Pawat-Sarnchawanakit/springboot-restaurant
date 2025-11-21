import { useState } from "react"
import api from '../api/axios'

export default function CreateRestaurant() {
    const [name, setName] = useState("");
    const [rating, setRating] = useState("");
    const [location, setLocation] = useState("");

    function submitRestaurant() {
        api.post("/api/restaurants", {
            name: name,
            rating: rating,
            location: location
        }).then(_ => {
            alert("OK");
        }).catch(err => {
            alert(err);
        });
    }
    
    return <div>
        <span>Name</span><br></br>
        <input type="text" required onChange={(e) => setName(e.target.value)}></input><br></br>
        <span>Rating</span><br></br>
        <input type="text" required onChange={(e) => setRating(e.target.value)}></input><br></br>
        <span>Location</span><br></br>
        <input type="text" required onChange={(e) => setLocation(e.target.value)}></input><br></br>
        <button onClick={submitRestaurant}>Submit</button>
    </div>
}