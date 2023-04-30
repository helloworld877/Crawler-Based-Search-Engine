// Home component

import { useRouter } from "next/router";
import { useState } from "react";
import styles from "../styles/Home.module.css";

// handleClick for search button

function Home(params) {
    const router = useRouter();
    const [query, setQuery] = useState();

    // go to query route
    const handleClick = () => {
        router.push(`/query/?q=${query}`);
    };
    // handle change of query when typing
    const handleChange = (event) => {
        setQuery(event.target.value);
    };
    return (
        <div className="h-100 d-flex align-items-center justify-content-center">
            <div className="container ">
                <div>
                    <div className="col">
                        <div>
                            <h1 className={styles.Logo}>JOOJLE</h1>
                            <button onClick={handleClick}>Search</button>
                            <input
                                type="text"
                                value={query}
                                onChange={handleChange}
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Home;
