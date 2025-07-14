package telran.java58.accounting.service;

import org.mindrot.jbcrypt.BCrypt;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import telran.java58.accounting.dao.UserAccountRepository;
import telran.java58.accounting.dto.RolesDto;
import telran.java58.accounting.dto.UserDto;
import telran.java58.accounting.dto.UserEditDto;
import telran.java58.accounting.dto.UserRegisterDto;
import telran.java58.accounting.dto.exception.InvalidDataException;
import telran.java58.accounting.dto.exception.UserExistsException;
import telran.java58.accounting.dto.exception.UserNotFoundException;
import telran.java58.accounting.model.Role;
import telran.java58.accounting.model.UserAccount;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner {
    private final UserAccountRepository userAccountRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
            throw new UserExistsException();
        }
        UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
        userAccount.addRole("USER");
        String password = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto removeUser(String login) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        userAccountRepository.delete(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        if (userEditDto.getFirstName() != null) {
            userAccount.setFirstName(userEditDto.getFirstName());
        }
        if (userEditDto.getLastName() != null) {
            userAccount.setLastName(userEditDto.getLastName());
        }
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        try {
            if (isAddRole) {
                userAccount.addRole(role);
            } else {
                userAccount.removeRole(role);
            }
        } catch (Exception e) {
            throw new InvalidDataException();
        }
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, RolesDto.class);
    }

    @Override
    public void changePassword(String login, String newPassword) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        userAccount.setPassword(hashedPassword);
        userAccountRepository.save(userAccount);
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userAccountRepository.existsById("admin")) {
            UserAccount admin = UserAccount.builder()
                    .login("admin")
                    .password(BCrypt.hashpw("admin", BCrypt.gensalt()))
                    .firstName("Admin")
                    .lastName("Admin")
                    .role(Role.USER)
                    .role(Role.MODERATOR)
                    .role(Role.ADMINISTRATOR)
                    .build();
            userAccountRepository.save(admin);
        }
    }
}
