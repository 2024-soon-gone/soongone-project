import { View, Button, Text, StyleSheet, ScrollView } from 'react-native';
import Logo from '../../assets/icon/logo-literal';
import Bell from '../../assets/icon/bell';
import theme from '../../assets/Theme';
import Feed from './Components/Feed';

const Home = () => {
  return (
    <View style={styles.root}>
      <View style={styles.header}>
        <Logo />
        <Bell />
      </View>
      <ScrollView>
        <Feed />
        <Feed />
        <Feed />
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    backgroundColor: theme.white,
    flex: 1,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
  },
});

export default Home;
