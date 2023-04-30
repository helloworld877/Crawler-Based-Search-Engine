// Home component

import { useRouter } from "next/router";
import { useState } from "react";
import styles from "../styles/Home.module.css";

// font awesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

// handleClick for search button

function Home(params) {
    const router = useRouter();
    const [query, setQuery] = useState();

    // go to query route
    const handleClick = () => {
        if (typeof query === "string") {
            router.push(`/query/?q=${query}`);
        }
    };
    // handle change of query when typing
    const handleChange = (event) => {
        setQuery(event.target.value);
    };
    return (
        <div className="h-100 d-flex align-items-center justify-content-center">
            <div className="container text-center ">
                <div>
                    <div className="col">
                        <div>
                            <h1 className={styles.Logo}>JOOJLE</h1>
                        </div>
                    </div>
                    <div className="col">
                        <div class="row height d-flex justify-content-center align-items-center">
                            <div class="col-md-6">
                                <div class={styles.form}>
                                    <FontAwesomeIcon
                                        icon={faMagnifyingGlass}
                                        className={styles.faSearch}
                                    />
                                    <input
                                        type="text"
                                        class={`${styles.formControl} ${styles.formInput}`}
                                        placeholder="Search anything..."
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col">
                        <button
                            type="button"
                            onClick={handleClick}
                            className={styles.pillButton}
                        >
                            Search
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Home;
