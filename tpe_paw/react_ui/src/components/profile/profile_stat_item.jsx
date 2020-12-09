const ProfileStatItem = (props) => {
  return (
    <div className="mr-3">
      <span className="profile-stat profile-stat-num">{props.itemCount} </span>
      <span className="profile-stat">{props.itemName}</span>
    </div>
  );
};

export default ProfileStatItem;
