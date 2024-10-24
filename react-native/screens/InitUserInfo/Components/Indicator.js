import { Pressable, StyleSheet } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';
import { View } from 'react-native';
import theme from '../../../assets/Theme';

function Indicator({ total, active }) {
  const circles = () => {
    var arr = [];
    for (let i = 0; i < total; i++) {
      arr.push(
        <Icon
          name="circle"
          style={styles.circle}
          key={i}
          color={active == i ? theme.grey4 : theme.grey2}
        />,
      );
    }
    return arr;
  };

  return <View style={styles.container} children={circles()} />;
}
const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  circle: {
    marginRight: 14,
  },
});

export default Indicator;
