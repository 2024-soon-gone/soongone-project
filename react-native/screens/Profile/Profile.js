import { View, Text, Pressable, Image, StyleSheet } from 'react-native';
import typo from '../../assets/Typograph';
import ProfileIndicator from './Components/ProfileIndicator';
import theme from '../../assets/Theme';

const Profile = () => {
  return (
    <View style={styles.root}>
      <View style={styles.header}>
        <View style={styles.header_top}>
          <Text style={styles.id}>soongone_</Text>
          <View style={styles.film}>
            <Image
              source={require('../../assets/icon/film.png')}
              style={styles.filmImg}
            />
            <Text style={styles.filmCount}>113,890</Text>
          </View>
        </View>
        <View style={styles.header_mid}>
          <Image
            source={require('../../assets/favicon.png')}
            style={styles.profileImg}
          />
          <View style={styles.indicators}>
            <ProfileIndicator value={0} label={'게시물'} />
            <ProfileIndicator value={204} label={'팔로워'} />
            <ProfileIndicator value={132} label={'팔로잉'} />
          </View>
        </View>
        <View style={styles.header_bottom}>
          <View style={styles.introductions}>
            <Text style={styles.name}>이순간</Text>
            <Text style={styles.introduction}>soon gone~~~</Text>
          </View>
          <View style={styles.buttons}>
            <Pressable style={styles.button}>
              <Text style={{ ...typo.label_light }}>프로필 수정</Text>
            </Pressable>
            <Pressable style={styles.button}>
              <Text style={{ ...typo.label_light }}>정보 수정</Text>
            </Pressable>
          </View>
        </View>
      </View>
    </View>
  );
};
const styles = StyleSheet.create({
  root: {
    flex: 1,
    width: '100%',
  },
  header: {
    width: '100%',
    height: 194,
    backgroundColor: 'white',
  },
  header_top: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    height: 32,
    paddingHorizontal: 20,
  },
  film: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  id: {
    alignItems: 'center',
    justifyContent: 'center',
    ...typo.H4,
    height: '100%',
  },
  filmImg: {
    width: 24,
    height: 24,
    marginEnd: 6,
  },
  filmCount: { ...typo.body },
  header_mid: {
    marginVertical: 12,
    flexDirection: 'row',
    paddingHorizontal: 20,
    justifyContent: 'space-between',
    width: '100%',
  },
  indicators: {
    flexDirection: 'row',
  },
  profileImg: {
    width: 60,
    height: 60,
    borderRadius: 30,
  },
  header_bottom: {
    height: 68,
    width: '100%',
    paddingHorizontal: 20,
    flexDirection: 'row',
    gap: 6,
  },
  introductions: {
    flex: 1,
    gap: 4,
  },
  name: {
    ...typo.label_medium,
  },
  introduction: {
    ...typo.label_medium,
  },
  buttons: {
    gap: 4,
    alignItems: 'center',
    justifyContent: 'space-evenly',
  },
  button: {
    width: 120,
    height: 32,
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: theme.grey1,
  },
});

export default Profile;
