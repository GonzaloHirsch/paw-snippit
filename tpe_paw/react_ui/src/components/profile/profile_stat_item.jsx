const ProfileStatItem = (props) => {
  return (
    <div className="mx-2 flex-center flex-col">
      <span className="fw-200 profile-stat-num">{props.itemCount} </span>
      <span className="fw-300 profile-stat">{props.itemName}</span>
    </div>
  );
};

export default ProfileStatItem;
