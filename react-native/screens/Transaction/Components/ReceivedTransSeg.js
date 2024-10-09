import { View, StyleSheet, Text, ScrollView, Image } from 'react-native';
import ReceivedTransRow from './ReceivedTransRow';
const ReceivedTransSeg = ({ data }) => {
  return (
    <View style={styles.root}>
      <ScrollView
        horizontal={true}
        style={styles.header}
        showsHorizontalScrollIndicator={false}
      >
        <Image source={require('../../../assets/favicon.png')} />
        <Image source={require('../../../assets/favicon.png')} />
        <Image source={require('../../../assets/favicon.png')} />
        <Image source={require('../../../assets/favicon.png')} />
        <Image source={require('../../../assets/favicon.png')} />
        <Image source={require('../../../assets/favicon.png')} />
        <Image source={require('../../../assets/favicon.png')} />
        <Image source={require('../../../assets/favicon.png')} />
        <Image source={require('../../../assets/favicon.png')} />
        <Image source={require('../../../assets/favicon.png')} />
      </ScrollView>
      <View style={{ width: '100%' }}>
        {data.map((trans, i) => (
          <ReceivedTransRow data={trans} key={i} />
        ))}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
  },
  header: {
    height: 72,
    width: '100%',
  },
});

export default ReceivedTransSeg;
