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
                        <div className="row height d-flex justify-content-center align-items-center">
                            <div className="col-md-6">
                                <div className={styles.form}>
                                    <FontAwesomeIcon
                                        icon={faMagnifyingGlass}
                                        className={styles.faSearch}
                                    />
                                    <input
                                        type="text"
                                        list="suggestions"
                                        className={`${styles.formControl} ${styles.formInput}`}
                                        placeholder="Search anything..."
                                        onChange={handleChange}
                                    />
                                    <datalist id="suggestions">
                                        <option value="activity" />
                                        <option value="ambition" />
                                        <option value="ad" />
                                        <option value="affair" />
                                        <option value="addition" />
                                        <option value="bird" />
                                        <option value="bathroom" />
                                        <option value="breath" />
                                        <option value="bedroom" />
                                        <option value="boyfriend" />
                                        <option value="celebration" />
                                        <option value="candidate" />
                                        <option value="city" />
                                        <option value="consequence" />
                                        <option value="control" />
                                        <option value="decision" />
                                        <option value="driver" />
                                        <option value="drama" />
                                        <option value="death" />
                                        <option value="debt" />
                                        <option value="environment" />
                                        <option value="explanation" />
                                        <option value="engineering" />
                                        <option value="election" />
                                        <option value="examination" />
                                        <option value="freedom" />
                                        <option value="feedback" />
                                        <option value="football" />
                                        <option value="fact" />
                                        <option value="food" />
                                        <option value="guitar" />
                                        <option value="goal" />
                                        <option value="girl" />
                                        <option value="government" />
                                        <option value="guidance" />
                                        <option value="housing" />
                                        <option value="homework" />
                                        <option value="hat" />
                                        <option value="heart" />
                                        <option value="health" />
                                        <option value="introduction" />
                                        <option value="independence" />
                                        <option value="intention" />
                                        <option value="interaction" />
                                        <option value="initiative" />
                                        <option value="job" />
                                        <option value="jet" />
                                        <option value="jelly" />
                                        <option value="journal" />
                                        <option value="jam" />
                                        <option value="knowledge" />
                                        <option value="kettle" />
                                        <option value="kidnap" />
                                        <option value="key" />
                                        <option value="knit" />
                                        <option value="law" />
                                        <option value="lane" />
                                        <option value="language" />
                                        <option value="lemon" />
                                        <option value="lazy" />
                                        <option value="meal" />
                                        <option value="modernize" />
                                        <option value="model" />
                                        <option value="mountain" />
                                        <option value="magnetic" />
                                        <option value="nationalist" />
                                        <option value="new" />
                                        <option value="nationalism" />
                                        <option value="navy" />
                                        <option value="nomination" />
                                        <option value="occasion" />
                                        <option value="offer" />
                                        <option value="occupy" />
                                        <option value="orbit" />
                                        <option value="old" />
                                        <option value="priority" />
                                        <option value="penetrate" />
                                        <option value="panel" />
                                        <option value="posture" />
                                        <option value="prediction" />
                                        <option value="quarter" />
                                        <option value="quest" />
                                        <option value="queue" />
                                        <option value="queen" />
                                        <option value="question" />
                                        <option value="reject" />
                                        <option value="retailer" />
                                        <option value="rehearsal" />
                                        <option value="recommend" />
                                        <option value="runner" />
                                        <option value="snap" />
                                        <option value="strength" />
                                        <option value="swipe" />
                                        <option value="similar" />
                                        <option value="settle" />
                                        <option value="thick" />
                                        <option value="transport" />
                                        <option value="therapist" />
                                        <option value="trace" />
                                        <option value="television" />
                                        <option value="unpleasant" />
                                        <option value="unlawful" />
                                        <option value="unrest" />
                                        <option value="unique" />
                                        <option value="uniform" />
                                        <option value="visit" />
                                        <option value="vegetable" />
                                        <option value="valid" />
                                        <option value="variable" />
                                        <option value="vote" />
                                        <option value="wardrobe" />
                                        <option value="whole" />
                                        <option value="withdrawal" />
                                        <option value="wrestle" />
                                        <option value="world" />
                                        <option value="x-ray" />
                                        <option value="xylophone" />
                                        <option value="xerox" />
                                        <option value="xylem" />
                                        <option value="yearn" />
                                        <option value="year" />
                                        <option value="yard" />
                                        <option value="youth" />
                                        <option value="young" />
                                        <option value="zero" />
                                        <option value="zone" />
                                        <option value="zigzag" />
                                        <option value="zephyr" />
                                        <option value="zenith" />
                                    </datalist>
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
